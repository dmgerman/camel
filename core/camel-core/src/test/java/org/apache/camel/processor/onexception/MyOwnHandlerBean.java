begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|onexception
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|MyOwnHandlerBean
specifier|public
class|class
name|MyOwnHandlerBean
block|{
DECL|field|payload
specifier|private
name|String
name|payload
decl_stmt|;
DECL|method|handleFailure (String payload)
specifier|public
name|void
name|handleFailure
parameter_list|(
name|String
name|payload
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|payload
operator|.
name|indexOf
argument_list|(
literal|"Error"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Damn something did not work"
argument_list|)
throw|;
block|}
name|this
operator|.
name|payload
operator|=
name|payload
expr_stmt|;
block|}
DECL|method|getPayload ()
specifier|public
name|String
name|getPayload
parameter_list|()
block|{
return|return
name|payload
return|;
block|}
block|}
end_class

end_unit

