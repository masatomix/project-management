#!/bin/bash

export JAVA_HOME=/opt/jdk1.5.0_12
export HOME_DIR=`echo $HOME`

# MailFilterを置いたディレクトリに置き換える
export LIBDIR=${HOME_DIR}/MailFilter/lib

# クラスパスの設定 ${LIBDIR} 内のjarすべて通す
jarList=`ls ${LIBDIR} | grep .jar`
export CLASSPATH=${LIBDIR}
for jarFile in ${jarList}
do
  # echo  ${LIBDIR}/${jarFile}
  export CLASSPATH=${CLASSPATH}:${LIBDIR}/${jarFile}
done

export PATH=${JAVA_HOME}/bin:${PATH}
# クラスパスの設定 以上


# 標準入力から値を取得
(
tmp="";

# 区切り文字の変更 tabが無視されるのを回避している
IFS=$'\n';
while read line
do
 tmp="${tmp}${line}\n";
done

if 
  echo -en "$tmp" | java  nu.mine.kino.mail.impl.Main
then
#  echo -en "$tmp" | ${HOME_DIR}/post2blog.pl
  echo -en "$tmp" | ${HOME_DIR}/MailFilter/stub.sh
fi
)

#-----------------------
#java  nu.mine.kino.mail.impl.Main

exit 0