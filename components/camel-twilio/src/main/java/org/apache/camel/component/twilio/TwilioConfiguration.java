begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twilio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twilio
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
name|RuntimeCamelException
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
name|twilio
operator|.
name|internal
operator|.
name|TwilioApiName
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParams
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Component configuration for Twilio component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|TwilioConfiguration
specifier|public
class|class
name|TwilioConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|apiName
specifier|private
name|TwilioApiName
name|apiName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"create,delete,fetch,read,update"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|TwilioConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|TwilioConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * What kind of operation to perform      *      * @return the API Name      */
DECL|method|getApiName ()
specifier|public
name|TwilioApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
comment|/**      * What kind of operation to perform      *      * @param apiName      *            the API Name to set      */
DECL|method|setApiName (TwilioApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|TwilioApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
comment|/**      * What sub operation to use for the selected operation      *      * @return the methodName      */
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
comment|/**      * What sub operation to use for the selected operation      *      * @param methodName      *            the methodName to set      */
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
block|}
end_class

end_unit

