begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm.exceptions.cmresponse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
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
name|cm
operator|.
name|exceptions
operator|.
name|MessagingException
import|;
end_import

begin_class
DECL|class|CMResponseException
specifier|public
class|class
name|CMResponseException
extends|extends
name|MessagingException
block|{
DECL|method|CMResponseException ()
specifier|public
name|CMResponseException
parameter_list|()
block|{     }
DECL|method|CMResponseException (final String message)
specifier|public
name|CMResponseException
parameter_list|(
specifier|final
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|CMResponseException (final Throwable cause)
specifier|public
name|CMResponseException
parameter_list|(
specifier|final
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|CMResponseException (final String message, final Throwable cause)
specifier|public
name|CMResponseException
parameter_list|(
specifier|final
name|String
name|message
parameter_list|,
specifier|final
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|CMResponseException (final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
specifier|public
name|CMResponseException
parameter_list|(
specifier|final
name|String
name|message
parameter_list|,
specifier|final
name|Throwable
name|cause
parameter_list|,
specifier|final
name|boolean
name|enableSuppression
parameter_list|,
specifier|final
name|boolean
name|writableStackTrace
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|,
name|enableSuppression
argument_list|,
name|writableStackTrace
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

