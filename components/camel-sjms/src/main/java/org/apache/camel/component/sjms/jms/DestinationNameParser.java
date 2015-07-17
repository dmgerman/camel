begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * @author jkorab  */
end_comment

begin_class
DECL|class|DestinationNameParser
specifier|public
class|class
name|DestinationNameParser
block|{
DECL|method|isTopic (String destinationName)
specifier|public
name|boolean
name|isTopic
parameter_list|(
name|String
name|destinationName
parameter_list|)
block|{
if|if
condition|(
name|destinationName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"destinationName is null"
argument_list|)
throw|;
block|}
return|return
name|destinationName
operator|.
name|startsWith
argument_list|(
literal|"topic:"
argument_list|)
return|;
block|}
DECL|method|getShortName (String destinationName)
specifier|public
name|String
name|getShortName
parameter_list|(
name|String
name|destinationName
parameter_list|)
block|{
if|if
condition|(
name|destinationName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"destinationName is null"
argument_list|)
throw|;
block|}
return|return
name|destinationName
operator|.
name|substring
argument_list|(
name|destinationName
operator|.
name|lastIndexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

