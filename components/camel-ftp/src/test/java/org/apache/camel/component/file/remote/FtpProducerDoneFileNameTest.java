begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|FtpProducerDoneFileNameTest
specifier|public
class|class
name|FtpProducerDoneFileNameTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/done?password=admin"
return|;
block|}
annotation|@
name|Test
DECL|method|testProducerConstantDoneFileName ()
specifier|public
name|void
name|testProducerConstantDoneFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&doneFileName=done"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/hello.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|done
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/done"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Done file should exists"
argument_list|,
literal|true
argument_list|,
name|done
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerPrefixDoneFileName ()
specifier|public
name|void
name|testProducerPrefixDoneFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&doneFileName=done-${file:name}"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/hello.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|done
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/done-hello.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Done file should exists"
argument_list|,
literal|true
argument_list|,
name|done
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerExtDoneFileName ()
specifier|public
name|void
name|testProducerExtDoneFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&doneFileName=${file:name}.done"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/hello.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|done
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/hello.txt.done"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Done file should exists"
argument_list|,
literal|true
argument_list|,
name|done
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerReplaceExtDoneFileName ()
specifier|public
name|void
name|testProducerReplaceExtDoneFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&doneFileName=${file:name.noext}.done"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/hello.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|done
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/done/hello.done"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Done file should exists"
argument_list|,
literal|true
argument_list|,
name|done
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerInvalidDoneFileName ()
specifier|public
name|void
name|testProducerInvalidDoneFileName
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
name|getFtpUrl
argument_list|()
operator|+
literal|"&doneFileName=${file:parent}/foo"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ExpressionIllegalSyntaxException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ExpressionIllegalSyntaxException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Cannot resolve reminder: ${file:parent}/foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testProducerEmptyDoneFileName ()
specifier|public
name|void
name|testProducerEmptyDoneFileName
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
name|getFtpUrl
argument_list|()
operator|+
literal|"&doneFileName="
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|IllegalArgumentException
name|cause
init|=
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
decl_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"doneFileName must be specified and not empty"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

