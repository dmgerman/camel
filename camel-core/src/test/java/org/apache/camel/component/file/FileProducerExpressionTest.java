begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|ContextTestSupport
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

begin_comment
comment|/**  * Unit test for expression option for file producer.  */
end_comment

begin_class
DECL|class|FileProducerExpressionTest
specifier|public
class|class
name|FileProducerExpressionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/filelanguage"
argument_list|)
expr_stmt|;
block|}
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
literal|"myguidgenerator"
argument_list|,
operator|new
name|MyGuidGenerator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testProduceBeanByHeader ()
specifier|public
name|void
name|testProduceBeanByHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/filelanguage"
argument_list|,
literal|"Hello World"
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"${bean:myguidgenerator}.bak"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/filelanguage/123.bak"
argument_list|)
expr_stmt|;
block|}
DECL|method|testProduceBeanByExpression ()
specifier|public
name|void
name|testProduceBeanByExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"file://target/filelanguage?expression=${bean:myguidgenerator}.bak"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/filelanguage/123.bak"
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerDateByHeader ()
specifier|public
name|void
name|testProducerDateByHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/filelanguage"
argument_list|,
literal|"Hello World"
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"myfile-${date:now:yyyyMMdd}.txt"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|String
name|date
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
name|assertFileExists
argument_list|(
literal|"target/filelanguage/myfile-"
operator|+
name|date
operator|+
literal|".txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerDateByExpression ()
specifier|public
name|void
name|testProducerDateByExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"file://target/filelanguage?expression=myfile-${date:now:yyyyMMdd}.txt"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|String
name|date
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
name|assertFileExists
argument_list|(
literal|"target/filelanguage/myfile-"
operator|+
name|date
operator|+
literal|".txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerComplexByExpression ()
specifier|public
name|void
name|testProducerComplexByExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expression
init|=
literal|"../filelanguageinbox/myfile-${bean:myguidgenerator.guid}-${date:now:yyyyMMdd}.txt"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"file://target/filelanguage?expression="
operator|+
name|expression
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|String
name|date
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
name|assertFileExists
argument_list|(
literal|"target/filelanguageinbox/myfile-123-"
operator|+
name|date
operator|+
literal|".txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerSimpleWithHeaderByExpression ()
specifier|public
name|void
name|testProducerSimpleWithHeaderByExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/filelanguage?expression=myfile-${in.header.foo}.txt"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/filelanguage/myfile-abc.txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerWithDateHeader ()
specifier|public
name|void
name|testProducerWithDateHeader
parameter_list|()
throws|throws
name|Exception
block|{
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
name|Date
name|date
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/filelanguage?expression=mybirthday-${date:in.header.birthday:yyyyMMdd}.txt"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"birthday"
argument_list|,
name|date
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/filelanguage/mybirthday-19740420.txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFileExists (String filename)
specifier|private
specifier|static
name|void
name|assertFileExists
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|file
operator|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"File "
operator|+
name|filename
operator|+
literal|" should exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyGuidGenerator
specifier|public
class|class
name|MyGuidGenerator
block|{
DECL|method|guid ()
specifier|public
name|String
name|guid
parameter_list|()
block|{
return|return
literal|"123"
return|;
block|}
block|}
block|}
end_class

end_unit

