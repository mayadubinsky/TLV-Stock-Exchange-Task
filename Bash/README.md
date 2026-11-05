# extract

A Bash script that unpacks compressed files, regardless of their name or extension. The compression type is detected from the output of the `file` command, and the matching tool is invoked automatically.

## Usage

```
extract [-h] [-r] [-v] file [file...]
```

| Option | Description |
|--------|-------------|
| `-h` | Show the help message |
| `-r` | Recursive: traverse directories recursively and unpack each file |
| `-v` | Verbose: print each file decompressed, warn for each file NOT decompressed |

Each argument can be a file or a directory. Without `-r`, a directory is processed one level deep (subdirectories are skipped).

**Output:** prints `Decompressed N archive(s)`.
**Exit code:** the number of files that were NOT decompressed (capped at 255).

## Supported formats

| Format | Detected by `file` as | Tool |
|--------|-----------------------|------|
| gzip | `gzip compressed data` | `gunzip` |
| bzip2 | `bzip2 compressed data` | `bunzip2` |
| zip | `Zip archive data` | `unzip` |
| compress (.Z) | `compress'd data` | `uncompress` |

## Examples

```bash
./extract file.gz                 # unpack a single file
./extract -v a b c                # unpack several files, verbose
./extract dir                     # unpack the files directly inside dir
./extract -r -v dir               # unpack everything under dir, recursively
./extract -v data.bin; echo $?    # extension does not matter; print the exit code
```

## Behavior and design decisions

- **Detection is by content only.** File names and extensions are ignored; the script relies solely on `file -b`.
- **Files are unpacked in place, under their original name.** For gzip, bzip2 and compress, the tool runs with `-c` (read from stdin, write to stdout) into a temporary file in the same directory, which then replaces the original. This is why the extension does not matter (`gunzip` refuses files without a known suffix when run on a path). The temporary file is in the same directory so the final `mv` is atomic, and the original is untouched if the tool fails.
- **The file name and extension are never changed.** `data.gz` stays `data.gz`, but its content is now the decompressed data (a tool like `gunzip` would instead create `data` and delete `data.gz`).
- **zip is extracted next to the archive.** `unzip` extracts its contents into the archive's directory (overwriting existing files with the same name, as the assignment requires). The zip file itself is left in place.
- **Files that are not archives** are left untouched and counted as "not decompressed" (they are reflected in the exit code and, with `-v`, in a warning). The same applies to arguments that do not exist and to corrupted archives.
- **Hidden files** are processed, and empty directories are handled without errors (`dotglob` and `nullglob`).
- **Symbolic links to directories are never followed**, so the script cannot loop forever or leave the requested tree. A symbolic link to a regular file is treated as that file.
- **File names with spaces or commas** are supported; each argument is one file. Multiple files are separated by spaces, as passed by the shell (quote names that contain spaces). A comma-separated list such as `a.gz,b.gz` is not supported: the comma is treated as part of the file name.
- **Exit code is capped at 255**, since a process exit status is a single byte (otherwise 256 failures would wrap around to 0).
- Running the script a second time on an already-unpacked file reports it as "not decompressed", because it is no longer an archive.

## Adding a new format

Add one line to the `METHODS` array in the script. Nothing else needs to change.

```
"pattern in file output|kind|command"
```

- `pattern`: text to look for in the output of `file -b`.
- `kind`: `pipe` if the command reads stdin and writes stdout, `dir` if the command extracts into the current directory by itself.
- `command`: the command with its options.

Example, adding xz support:

```bash
"XZ compressed data|pipe|unxz -c"
```

Entries are checked in order, so put more specific patterns first.

## Testing

Manual test commands used during development:

```bash
# all formats, no extension, recursive
mkdir -p t/sub && cd t
echo hello > a.txt
bzip2 -c a.txt > bz_noext
zip -q z.zip a.txt && mv z.zip z_noext
compress -c a.txt > comp_noext
gzip -c a.txt > sub/gz_noext
rm a.txt && cd ..
./extract -v -r t; echo $?        # 4 extracted, exit code 0

# not an archive, plain text
echo plain > p.txt
./extract -v p.txt; echo $?       # warning, exit code 1
```
