begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExecutionException
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
name|Header
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
name|builder
operator|.
name|RouteBuilder
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
name|spi
operator|.
name|Synchronization
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FileRollbackOnCompletionTest
specifier|public
class|class
name|FileRollbackOnCompletionTest
extends|extends
name|ContextTestSupport
block|{
DECL|class|FileRollback
specifier|public
specifier|static
specifier|final
class|class
name|FileRollback
implements|implements
name|Synchronization
block|{
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// this method is invoked when the Exchange completed with no failure
block|}
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// delete the file
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_PRODUCED
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
operator|new
name|File
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|OrderService
specifier|public
specifier|static
specifier|final
class|class
name|OrderService
block|{
DECL|method|createMail (String order)
specifier|public
name|String
name|createMail
parameter_list|(
name|String
name|order
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|"Order confirmed: "
operator|+
name|order
return|;
block|}
DECL|method|sendMail (String body, @Header(R) String to)
specifier|public
name|void
name|sendMail
parameter_list|(
name|String
name|body
parameter_list|,
annotation|@
name|Header
argument_list|(
literal|"to"
argument_list|)
name|String
name|to
parameter_list|)
block|{
comment|// simulate fatal error if we refer to a special no
if|if
condition|(
name|to
operator|.
name|equals
argument_list|(
literal|"FATAL"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Simulated fatal error"
argument_list|)
throw|;
block|}
comment|// simulate CPU processing of the order by sleeping a bit
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/mail/backup"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testOk ()
specifier|public
name|void
name|testOk
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:confirm"
argument_list|,
literal|"bumper"
argument_list|,
literal|"to"
argument_list|,
literal|"someone@somewhere.org"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/mail/backup/"
argument_list|)
decl_stmt|;
name|String
index|[]
name|files
init|=
name|file
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be one file"
argument_list|,
literal|1
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|testRollback ()
specifier|public
name|void
name|testRollback
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:confirm"
argument_list|,
literal|"bumper"
argument_list|,
literal|"to"
argument_list|,
literal|"FATAL"
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
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Simulated fatal error"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
comment|// onCompletion is async so we gotta wait a bit for the file to be deleted
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/mail/backup/"
argument_list|)
decl_stmt|;
name|String
index|[]
name|files
init|=
name|file
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be no files"
argument_list|,
literal|0
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:confirm"
argument_list|)
comment|// use a route scoped onCompletion to be executed when the Exchange failed
operator|.
name|onCompletion
argument_list|()
operator|.
name|onFailureOnly
argument_list|()
comment|// and call the onFailure method on this bean
operator|.
name|bean
argument_list|(
name|FileRollback
operator|.
name|class
argument_list|,
literal|"onFailure"
argument_list|)
comment|// must use end to denote the end of the onCompletion route
operator|.
name|end
argument_list|()
comment|// here starts the regular route
operator|.
name|bean
argument_list|(
name|OrderService
operator|.
name|class
argument_list|,
literal|"createMail"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Saving mail backup file"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/mail/backup"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Trying to send mail to ${header.to}"
argument_list|)
operator|.
name|bean
argument_list|(
name|OrderService
operator|.
name|class
argument_list|,
literal|"sendMail"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Mail send to ${header.to}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

