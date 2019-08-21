begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium.configuration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
operator|.
name|configuration
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ConfigurationValidation
specifier|public
specifier|final
class|class
name|ConfigurationValidation
block|{
DECL|field|isValid
specifier|private
specifier|final
name|boolean
name|isValid
decl_stmt|;
DECL|field|reason
specifier|private
specifier|final
name|String
name|reason
decl_stmt|;
DECL|method|ConfigurationValidation (final boolean isValid, final String reason)
specifier|private
name|ConfigurationValidation
parameter_list|(
specifier|final
name|boolean
name|isValid
parameter_list|,
specifier|final
name|String
name|reason
parameter_list|)
block|{
name|this
operator|.
name|isValid
operator|=
name|isValid
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
DECL|method|valid ()
specifier|public
specifier|static
name|ConfigurationValidation
name|valid
parameter_list|()
block|{
return|return
operator|new
name|ConfigurationValidation
argument_list|(
literal|true
argument_list|,
literal|""
argument_list|)
return|;
block|}
DECL|method|notValid (final String reason)
specifier|public
specifier|static
name|ConfigurationValidation
name|notValid
parameter_list|(
specifier|final
name|String
name|reason
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|reason
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You will need to specify a reason why is not valid"
argument_list|)
throw|;
block|}
return|return
operator|new
name|ConfigurationValidation
argument_list|(
literal|false
argument_list|,
name|reason
argument_list|)
return|;
block|}
DECL|method|isValid ()
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|isValid
return|;
block|}
DECL|method|getReason ()
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|reason
return|;
block|}
block|}
end_class

end_unit

