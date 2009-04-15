begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|TransactedRuntimeCamelException
specifier|public
class|class
name|TransactedRuntimeCamelException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|handled
specifier|private
name|boolean
name|handled
decl_stmt|;
DECL|method|TransactedRuntimeCamelException ()
specifier|public
name|TransactedRuntimeCamelException
parameter_list|()
block|{     }
DECL|method|TransactedRuntimeCamelException (String message)
specifier|public
name|TransactedRuntimeCamelException
parameter_list|(
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
DECL|method|TransactedRuntimeCamelException (String message, Throwable cause)
specifier|public
name|TransactedRuntimeCamelException
parameter_list|(
name|String
name|message
parameter_list|,
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
DECL|method|TransactedRuntimeCamelException (Throwable cause)
specifier|public
name|TransactedRuntimeCamelException
parameter_list|(
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
DECL|method|isHandled ()
specifier|public
name|boolean
name|isHandled
parameter_list|()
block|{
return|return
name|handled
return|;
block|}
DECL|method|setHandled (boolean handled)
specifier|public
name|void
name|setHandled
parameter_list|(
name|boolean
name|handled
parameter_list|)
block|{
name|this
operator|.
name|handled
operator|=
name|handled
expr_stmt|;
block|}
block|}
end_class

end_unit

