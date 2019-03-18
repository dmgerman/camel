begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|TestMessages
specifier|public
class|class
name|TestMessages
block|{
DECL|field|instance
specifier|private
specifier|static
name|TestMessages
name|instance
decl_stmt|;
DECL|field|messages
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|getInstance ()
specifier|public
specifier|static
name|TestMessages
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|TestMessages
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
DECL|method|getMessages ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getMessages
parameter_list|()
block|{
return|return
name|messages
return|;
block|}
DECL|method|addMessage (Object message)
specifier|public
name|void
name|addMessage
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

