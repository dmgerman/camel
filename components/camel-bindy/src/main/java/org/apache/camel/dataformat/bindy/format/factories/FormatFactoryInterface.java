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
name|java
operator|.
name|util
operator|.
name|Collection
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
name|dataformat
operator|.
name|bindy
operator|.
name|Format
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
name|dataformat
operator|.
name|bindy
operator|.
name|FormattingOptions
import|;
end_import

begin_interface
DECL|interface|FormatFactoryInterface
specifier|public
interface|interface
name|FormatFactoryInterface
block|{
comment|/**      * Returns the list of supported classes.      * When the list doesn't contain elements the factory is supposed      * to support all kinds of classes. The factory must decide on other      * criteria whether it can build a {@link Format}.      * @return the list of supported classes      */
DECL|method|supportedClasses ()
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|supportedClasses
parameter_list|()
function_decl|;
comment|/**      * Can it build a {@link Format}.      * Answers the question about whether it can      * build a {@link Format}.      * @param formattingOptions      * @return can build      */
DECL|method|canBuild (FormattingOptions formattingOptions)
name|boolean
name|canBuild
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
function_decl|;
comment|/**      * Builds the {@link Format}.      * @param formattingOptions      * @return the format      */
DECL|method|build (FormattingOptions formattingOptions)
name|Format
argument_list|<
name|?
argument_list|>
name|build
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

