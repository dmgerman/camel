begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
package|;
end_package

begin_class
DECL|class|TestApiRequestor
specifier|public
class|class
name|TestApiRequestor
implements|implements
name|ApiRequestor
block|{
DECL|field|body
name|String
name|body
decl_stmt|;
DECL|method|TestApiRequestor (String body)
specifier|public
name|TestApiRequestor
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|send ()
specifier|public
name|String
name|send
parameter_list|()
block|{
return|return
name|body
return|;
block|}
block|}
end_class

end_unit

