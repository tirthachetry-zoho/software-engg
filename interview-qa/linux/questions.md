# Linux — Interview Q&A

## 1. How do you find the top CPU-consuming processes?
**Answer:** `ps -eo pid,ppid,cmd,%mem,%cpu --sort=-%cpu | head` lists processes by CPU desc. Or interactively `top`/`htop`. For a Java process, pair with `jstack` to find the hot thread.

## 2. How do you find what is listening on a port?
**Answer:** `ss -ltnp | grep :8080` (modern) or `netstat -ltnp | grep :8080`. The `-p` shows the owning PID/program. Use `lsof -i :8080` as an alternative.

## 3. What does `grep`, `awk`, and `sed` do?
**Answer:** `grep` filters lines by pattern. `awk` is a column-processing language (e.g., `awk '{print $1, $3}'`). `sed` is a stream editor for substitutions (`sed 's/old/new/g'`). Together they're the text-processing trio for logs.

## 4. How do you count occurrences (e.g., 5xx errors in a log)?
**Answer:** `grep -oE '" [0-9]{3} ' access.log | grep -E '" 5[0-9]{2} ' | wc -l` — extract status codes, filter 5xx, count lines. Or `awk` to tally per code. `wc -l` counts lines.

## 5. What is the difference between `kill`, `kill -9`, and `kill -15`?
**Answer:** `kill PID` sends **SIGTERM (15)** — graceful shutdown (app can clean up). `kill -9` sends **SIGKILL** — immediate, un-catchable termination (use only if stuck). Prefer SIGTERM; SIGKILL can corrupt state.

## 6. How do you monitor real-time system resources?
**Answer:** `top`/`htop` (CPU/mem/processes), `vmstat` (system-wide), `iostat` (disk I/O), `free -h` (memory), `df -h` (disk), `sar` (historical). For networks: `iftop`, `ss`.

## 7. What is the difference between `stdout`, `stderr`, and redirection?
**Answer:** FD 1 = stdout (normal output), FD 2 = stderr (errors). `> file` redirects stdout; `2> err` redirects stderr; `2>&1` merges; `&> file` captures both. `command | tee file` shows and saves.

## 8. How do you find and kill a process by name?
**Answer:** `pgrep -f "java.*myapp"` to find PIDs, then `pkill -f "java.*myapp"`. Or `ps aux | grep pattern`. Be careful with `-f` (matches full cmdline) to avoid killing the wrong process.

## 9. What is the difference between a process and a thread in Linux?
**Answer:** A **process** has its own memory (PID); threads (**LWP**) share the process's memory (same PID, different TID). Threads are lighter to create/context-switch. `ps -eLf` shows threads (LWP column).

## 10. How do you inspect open files / file descriptors?
**Answer:** `lsof -p PID` lists all files/sockets a process has open (great for "too many open files" / `EMFILE`). `lsof -i` shows network files. FD limits via `ulimit -n`.

## 11. What does `chmod`, `chown`, and permissions `755` mean?
**Answer:** `chmod` sets permission bits; `chown` changes owner/group. `755` = `rwxr-xr-x`: owner read/write/execute, group/others read/execute. First digit = owner, second = group, third = others (r=4,w=2,x=1).

## 12. How do you debug a "disk full" or "no space left" issue?
**Answer:** `df -h` to find the full filesystem; `du -sh /*` (or `ncdu`) to locate big directories; check for deleted-but-open files (`lsof | grep deleted`) holding space; clear logs/old files; verify inode exhaustion with `df -i`.