begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
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
name|processor
operator|.
name|Pipeline
import|;
end_import

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
name|Collections
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|FileExchangeTest
specifier|public
class|class
name|FileExchangeTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|file
specifier|protected
name|File
name|file
decl_stmt|;
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
block|{
name|FileExchange
name|fileExchange
init|=
operator|new
name|FileExchange
argument_list|(
name|context
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|fileExchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|FileExchange
name|copy
init|=
name|assertIsInstanceOf
argument_list|(
name|FileExchange
operator|.
name|class
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File"
argument_list|,
name|file
argument_list|,
name|copy
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|copy
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a body!"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|testPipelineCopy ()
specifier|public
name|void
name|testPipelineCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|Processor
name|myProcessor
init|=
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a body!"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Pipeline
name|pipeline
init|=
operator|new
name|Pipeline
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|myProcessor
argument_list|)
argument_list|)
decl_stmt|;
name|FileExchange
name|exchange
init|=
operator|new
name|FileExchange
argument_list|(
name|context
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|pipeline
operator|.
name|process
argument_list|(
name|exchange
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|file
operator|=
operator|new
name|File
argument_list|(
name|FileExchangeTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"FileExchangeTest.class"
argument_list|)
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

