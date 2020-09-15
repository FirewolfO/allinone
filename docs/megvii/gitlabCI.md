```powershell
test "" = "$(grep '^Signed-off-by: ' "$1" |
         sort | uniq -c | sed -e '/^[   ]*1[    ]/d')" || {
        echo >&2 Duplicate Signed-off-by lines.
        exit 1
}
```



```powershell
hooks地址：
/var/opt/gitlab/git-data/repositories/@hashed/6b/86/6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b.git/hooks
```

```powershell
你要是在gitlab服务器没有我给你发的目录，就直接找一个 find / -type d -name  hooks  
```

