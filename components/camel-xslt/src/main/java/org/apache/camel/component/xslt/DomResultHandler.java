begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMResult
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

begin_comment
comment|/**  * Uses DOM to handle results of the transformation  */
end_comment

begin_class
DECL|class|DomResultHandler
specifier|public
class|class
name|DomResultHandler
implements|implements
name|ResultHandler
block|{
DECL|field|result
specifier|private
name|DOMResult
name|result
init|=
operator|new
name|DOMResult
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getResult ()
specifier|public
name|Result
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|setBody (Message in)
specifier|public
name|void
name|setBody
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
name|in
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

