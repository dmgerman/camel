begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
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
name|format
operator|.
name|factories
operator|.
name|FactoryRegistry
import|;
end_import

begin_comment
comment|/**  * Factory to return {@link Format} classes for a given type.  */
end_comment

begin_class
DECL|class|FormatFactory
specifier|public
specifier|final
class|class
name|FormatFactory
block|{
DECL|field|factoryRegistry
specifier|private
name|FactoryRegistry
name|factoryRegistry
decl_stmt|;
DECL|method|FormatFactory ()
specifier|public
name|FormatFactory
parameter_list|()
block|{     }
DECL|method|doGetFormat (FormattingOptions formattingOptions)
specifier|private
name|Format
argument_list|<
name|?
argument_list|>
name|doGetFormat
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
block|{
return|return
name|factoryRegistry
operator|.
name|findForFormattingOptions
argument_list|(
name|formattingOptions
argument_list|)
operator|.
name|build
argument_list|(
name|formattingOptions
argument_list|)
return|;
block|}
comment|/**      * Retrieves the format to use for the given type*      */
DECL|method|getFormat (FormattingOptions formattingOptions)
specifier|public
name|Format
argument_list|<
name|?
argument_list|>
name|getFormat
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|formattingOptions
operator|.
name|getBindyConverter
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|formattingOptions
operator|.
name|getBindyConverter
argument_list|()
operator|.
name|value
argument_list|()
operator|.
name|newInstance
argument_list|()
return|;
block|}
return|return
name|doGetFormat
argument_list|(
name|formattingOptions
argument_list|)
return|;
block|}
DECL|method|setFactoryRegistry (FactoryRegistry factoryRegistry)
specifier|public
name|void
name|setFactoryRegistry
parameter_list|(
name|FactoryRegistry
name|factoryRegistry
parameter_list|)
block|{
name|this
operator|.
name|factoryRegistry
operator|=
name|factoryRegistry
expr_stmt|;
block|}
DECL|method|getFactoryRegistry ()
specifier|public
name|FactoryRegistry
name|getFactoryRegistry
parameter_list|()
block|{
return|return
name|factoryRegistry
return|;
block|}
block|}
end_class

end_unit

