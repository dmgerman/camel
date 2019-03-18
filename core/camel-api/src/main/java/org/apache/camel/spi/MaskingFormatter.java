begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_comment
comment|/**  * A plugin used to mask a log String, for example security information  * like password or passphrase.  */
end_comment

begin_interface
DECL|interface|MaskingFormatter
specifier|public
interface|interface
name|MaskingFormatter
block|{
DECL|field|CUSTOM_LOG_MASK_REF
name|String
name|CUSTOM_LOG_MASK_REF
init|=
literal|"CamelCustomLogMask"
decl_stmt|;
comment|/**      * Format a given string.      *      * @param source the source string      * @return formatted string      */
DECL|method|format (String source)
name|String
name|format
parameter_list|(
name|String
name|source
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

