jaf-1_1_1.zip
javamail-1_4_1.zip
を取得し、解凍してできる
activation.jar
dsn.jar
imap.jar
mail.jar
mailapi.jar
pop3.jar
smtp.jar
をlibディレクトリに配置してください。

libディレクトリには必要に応じて以下のファイルを配置してください。
・filter.properties
 フィルタ処理が記述されているフィルタの実装クラスを指定します。
 ここで指定したフィルタが実行されます。ファイルがなかった場合などはなんのフィルタも呼び出されません。

・mail.properties
 フィルタ例外が発生したときにメールを送信する場合のメールサーバの情報を記述します。
 このファイルがなかった場合はメール送信を行いません。

・whitelist.properties
 nu.mine.kino.mail.impl.AddressFilter クラスが参照するメールアドレスのホワイトリストです。
 ここに書いてあるメールアドレスは通過させます。
 このファイルがなかった場合は特に遮断処理を行いませんのでご注意ください。

・log4j.xml
 アプリケーションのデバッグログのための設定ファイルです。