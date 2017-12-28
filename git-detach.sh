#!/bin/bash
git checkout --orphan temp $1
git commit -m "截取的历史记录起点"
git rebase --onto temp $1 master
git branch -D temp