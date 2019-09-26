begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Properties
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
name|CamelContext
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * Unit test for writing done files  */
end_comment

begin_class
DECL|class|FilerProducerDoneFileNameTest
specifier|public
class|class
name|FilerProducerDoneFileNameTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myProp
specifier|private
name|Properties
name|myProp
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
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
literal|"target/data/done"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
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
literal|"myProp"
argument_list|,
name|myProp
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"ref:myProp"
argument_list|)
expr_stmt|;
return|return
name|context
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
literal|"file:target/data/done?doneFileName=done"
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
literal|"target/data/done/hello.txt"
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
literal|"target/data/done/done"
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
literal|"file:target/data/done?doneFileName=done-${file:name}"
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
literal|"target/data/done/hello.txt"
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
literal|"target/data/done/done-hello.txt"
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
literal|"file:target/data/done?doneFileName=${file:name}.done"
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
literal|"target/data/done/hello.txt"
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
literal|"target/data/done/hello.txt.done"
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
literal|"file:target/data/done?doneFileName=${file:name.noext}.done"
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
literal|"target/data/done/hello.txt"
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
literal|"target/data/done/hello.done"
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
literal|"file:target/data/done?doneFileName=${file:parent}/foo"
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
literal|"file:target/data/done?doneFileName="
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
annotation|@
name|Test
DECL|method|testProducerPlaceholderPrefixDoneFileName ()
specifier|public
name|void
name|testProducerPlaceholderPrefixDoneFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|myProp
operator|.
name|put
argument_list|(
literal|"myDir"
argument_list|,
literal|"target/data/done"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:{{myDir}}?doneFileName=done-${file:name}"
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
literal|"target/data/done/hello.txt"
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
literal|"target/data/done/done-hello.txt"
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
block|}
end_class

end_unit

