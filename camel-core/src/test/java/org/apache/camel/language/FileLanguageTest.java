begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExpressionIllegalSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|LanguageTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|FileConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|FileEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|GenericFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|FileUtil
import|;
end_import

begin_comment
comment|/**  * Unit test for File Language.  */
end_comment

begin_class
DECL|class|FileLanguageTest
specifier|public
class|class
name|FileLanguageTest
extends|extends
name|LanguageTestSupport
block|{
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"generator"
argument_list|,
operator|new
name|MyFileNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"file"
return|;
block|}
DECL|method|testConstantExpression ()
specifier|public
name|void
name|testConstantExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"MyBigFile.txt"
argument_list|,
literal|"MyBigFile.txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|testMessageId ()
specifier|public
name|void
name|testMessageId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${id}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${id}.bak"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
operator|+
literal|".bak"
argument_list|)
expr_stmt|;
block|}
DECL|method|testFile ()
specifier|public
name|void
name|testFile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"${file:ext}"
argument_list|,
literal|"txt"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:name.ext}"
argument_list|,
literal|"txt"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:name}"
argument_list|,
literal|"test"
operator|+
name|File
operator|.
name|separator
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:name.noext}"
argument_list|,
literal|"test"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"hello"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:onlyname}"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:onlyname.noext}"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:parent}"
argument_list|,
name|file
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:path}"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:absolute}"
argument_list|,
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:absolute.path}"
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:length}"
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"${file:size}"
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// modified is a Date object
name|Date
name|modified
init|=
name|SimpleLanguage
operator|.
name|simple
argument_list|(
literal|"file:modified"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|,
name|modified
argument_list|)
expr_stmt|;
block|}
DECL|method|testFileUsingAlternativeStartToken ()
specifier|public
name|void
name|testFileUsingAlternativeStartToken
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"$simple{file:ext}"
argument_list|,
literal|"txt"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:name.ext}"
argument_list|,
literal|"txt"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:name}"
argument_list|,
literal|"test"
operator|+
name|File
operator|.
name|separator
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:name.noext}"
argument_list|,
literal|"test"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"hello"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:onlyname}"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:onlyname.noext}"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:parent}"
argument_list|,
name|file
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:path}"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:absolute}"
argument_list|,
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:absolute.path}"
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:length}"
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$simple{file:size}"
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// modified is a Date object
name|Date
name|modified
init|=
name|SimpleLanguage
operator|.
name|simple
argument_list|(
literal|"file:modified"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|,
name|modified
argument_list|)
expr_stmt|;
block|}
DECL|method|testDate ()
specifier|public
name|void
name|testDate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|now
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd"
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
literal|"backup-${date:now:yyyyMMdd}"
argument_list|,
literal|"backup-"
operator|+
name|now
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd"
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
literal|"backup-${date:file:yyyyMMdd}"
argument_list|,
literal|"backup-"
operator|+
name|expected
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"backup-${date:header.birthday:yyyyMMdd}"
argument_list|,
literal|"backup-19740420"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"hello-${date:out.header.special:yyyyMMdd}"
argument_list|,
literal|"hello-20080808"
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|assertExpression
argument_list|(
literal|"nodate-${date:header.xxx:yyyyMMdd}"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testDateUsingAlternativeStartToken ()
specifier|public
name|void
name|testDateUsingAlternativeStartToken
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|now
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd"
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
literal|"backup-$simple{date:now:yyyyMMdd}"
argument_list|,
literal|"backup-"
operator|+
name|now
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd"
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertExpression
argument_list|(
literal|"backup-$simple{date:file:yyyyMMdd}"
argument_list|,
literal|"backup-"
operator|+
name|expected
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"backup-$simple{date:header.birthday:yyyyMMdd}"
argument_list|,
literal|"backup-19740420"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"hello-$simple{date:out.header.special:yyyyMMdd}"
argument_list|,
literal|"hello-20080808"
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|assertExpression
argument_list|(
literal|"nodate-$simple{date:header.xxx:yyyyMMdd}"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testSimpleAndFile ()
specifier|public
name|void
name|testSimpleAndFile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"backup-${in.header.foo}-${file:name.noext}.bak"
argument_list|,
literal|"backup-abc-test"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"hello.bak"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"backup-${in.header.foo}-${file:onlyname.noext}.bak"
argument_list|,
literal|"backup-abc-hello.bak"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleAndFileAndBean ()
specifier|public
name|void
name|testSimpleAndFileAndBean
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"backup-${in.header.foo}-${bean:generator}-${file:name.noext}.bak"
argument_list|,
literal|"backup-abc-generatorbybean-test"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"hello.bak"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"backup-${in.header.foo}-${bean:generator}-${file:onlyname.noext}.bak"
argument_list|,
literal|"backup-abc-generatorbybean-hello.bak"
argument_list|)
expr_stmt|;
block|}
DECL|method|testBean ()
specifier|public
name|void
name|testBean
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"backup-${bean:generator}.txt"
argument_list|,
literal|"backup-generatorbybean.txt"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"backup-${bean:generator.generateFilename}.txt"
argument_list|,
literal|"backup-generatorbybean.txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
comment|// create the file
name|String
name|uri
init|=
literal|"file://target/filelanguage?fileExist=Override"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"test/hello.txt"
argument_list|)
expr_stmt|;
comment|// get the file handle
name|file
operator|=
operator|new
name|File
argument_list|(
literal|"target/filelanguage/test/hello.txt"
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|gf
init|=
name|FileConsumer
operator|.
name|asGenericFile
argument_list|(
literal|"target/filelanguage"
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|FileEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|uri
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|answer
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|gf
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|configureMessage
argument_list|(
name|gf
argument_list|,
name|answer
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|Calendar
name|cal
init|=
name|GregorianCalendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|1974
argument_list|,
name|Calendar
operator|.
name|APRIL
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"birthday"
argument_list|,
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2008
argument_list|,
name|Calendar
operator|.
name|AUGUST
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"special"
argument_list|,
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|testIllegalSyntax ()
specifier|public
name|void
name|testIllegalSyntax
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// it should be with colon
name|assertExpression
argument_list|(
literal|"${file.name}"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Illegal syntax: file.name"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"hey ${xxx} how are you?"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Illegal syntax: xxx"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertExpression
argument_list|(
literal|"${xxx}"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionIllegalSyntaxException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Illegal syntax: xxx"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testConstantFilename ()
specifier|public
name|void
name|testConstantFilename
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"hello.txt"
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
block|}
DECL|class|MyFileNameGenerator
specifier|public
class|class
name|MyFileNameGenerator
block|{
DECL|method|generateFilename (Exchange exchange)
specifier|public
name|String
name|generateFilename
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|"generatorbybean"
return|;
block|}
block|}
block|}
end_class

end_unit

