#!/bin/bash
git checkout --orphan temp $1
git commit -m "��ȡ����ʷ��¼���"
git rebase --onto temp $1 master
git branch -D temp