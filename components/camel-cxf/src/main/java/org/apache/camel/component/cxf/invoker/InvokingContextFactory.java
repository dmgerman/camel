begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.invoker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|invoker
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
name|component
operator|.
name|cxf
operator|.
name|DataFormat
import|;
end_import

begin_class
DECL|class|InvokingContextFactory
specifier|public
specifier|final
class|class
name|InvokingContextFactory
block|{
DECL|method|InvokingContextFactory ()
specifier|private
name|InvokingContextFactory
parameter_list|()
block|{
comment|// not constructed
block|}
comment|/**      * Static method that creates a routing context object from a given data format      * @param dataFormat      * @return routing context      */
DECL|method|createContext (DataFormat dataFormat)
specifier|public
specifier|static
name|InvokingContext
name|createContext
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
if|if
condition|(
name|dataFormat
operator|==
name|DataFormat
operator|.
name|MESSAGE
condition|)
block|{
return|return
operator|new
name|RawMessageInvokingContext
argument_list|()
return|;
block|}
if|if
condition|(
name|dataFormat
operator|==
name|DataFormat
operator|.
name|PAYLOAD
condition|)
block|{
return|return
operator|new
name|PayloadInvokingContext
argument_list|()
return|;
block|}
comment|//Default is DataFormat.MESSAGE, we do not set the POJO context
return|return
operator|new
name|RawMessageInvokingContext
argument_list|()
return|;
block|}
block|}
end_class

end_unit

