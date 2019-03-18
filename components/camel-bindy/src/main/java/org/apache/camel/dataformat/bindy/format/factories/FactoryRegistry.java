begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.format.factories
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|format
operator|.
name|factories
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
name|dataformat
operator|.
name|bindy
operator|.
name|FormattingOptions
import|;
end_import

begin_interface
DECL|interface|FactoryRegistry
specifier|public
interface|interface
name|FactoryRegistry
block|{
DECL|method|register (FormatFactoryInterface... formatFactory)
name|FactoryRegistry
name|register
parameter_list|(
name|FormatFactoryInterface
modifier|...
name|formatFactory
parameter_list|)
function_decl|;
DECL|method|unregister (Class<? extends FormatFactoryInterface> clazz)
name|FactoryRegistry
name|unregister
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|FormatFactoryInterface
argument_list|>
name|clazz
parameter_list|)
function_decl|;
DECL|method|findForFormattingOptions (FormattingOptions formattingOptions)
name|FormatFactoryInterface
name|findForFormattingOptions
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

