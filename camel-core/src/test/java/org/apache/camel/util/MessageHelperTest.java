begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Message
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
name|converter
operator|.
name|stream
operator|.
name|StreamCache
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
name|DefaultMessage
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link MessageHelper}  */
end_comment

begin_class
DECL|class|MessageHelperTest
specifier|public
class|class
name|MessageHelperTest
extends|extends
name|TestCase
block|{
DECL|field|message
specifier|private
name|Message
name|message
decl_stmt|;
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
name|message
operator|=
operator|new
name|DefaultMessage
argument_list|()
expr_stmt|;
block|}
comment|/*      * Tests the {@link MessageHelper#resetStreamCache(Message)} method      */
DECL|method|testResetStreamCache ()
specifier|public
name|void
name|testResetStreamCache
parameter_list|()
throws|throws
name|Exception
block|{
comment|// should not throw exceptions when Message or message body is null
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
operator|(
name|Message
operator|)
literal|null
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// handle StreamCache
specifier|final
name|ValueHolder
argument_list|<
name|Boolean
argument_list|>
name|reset
init|=
operator|new
name|ValueHolder
argument_list|<
name|Boolean
argument_list|>
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|StreamCache
argument_list|()
block|{
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|reset
operator|.
name|set
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have reset the stream cache"
argument_list|,
name|reset
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

