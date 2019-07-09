begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.attachment
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|attachment
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|Message
import|;
end_import

begin_comment
comment|/**  * Represents an attachment as part of a {@link Message}.  */
end_comment

begin_interface
DECL|interface|Attachment
specifier|public
interface|interface
name|Attachment
block|{
comment|/**      * Return a DataHandler for the content within this attachment.      *      * @return DataHandler for the content      */
DECL|method|getDataHandler ()
name|DataHandler
name|getDataHandler
parameter_list|()
function_decl|;
comment|/**      * Get all the headers for this header name. Returns null if no headers for      * this header name are available.      *      * @param headerName he name of this header      * @return a comma separated list of all header values      */
DECL|method|getHeader (String headerName)
name|String
name|getHeader
parameter_list|(
name|String
name|headerName
parameter_list|)
function_decl|;
comment|/**      * Get all the headers for this header name. Returns null if no headers for      * this header name are available.      *      * @param name The name of this header      * @return a list of all header values      */
DECL|method|getHeaderAsList (String name)
name|List
argument_list|<
name|String
argument_list|>
name|getHeaderAsList
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Get all header names for this attachment.      *      * @return a collection of all header names      */
DECL|method|getHeaderNames ()
name|Collection
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
function_decl|;
comment|/**      * Set the value for this headerName. Replaces all existing header values      * with this new value.      *       * @param headerName the name of this header      * @param headerValue the value for this header      */
DECL|method|setHeader (String headerName, String headerValue)
name|void
name|setHeader
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|headerValue
parameter_list|)
function_decl|;
comment|/**      * Add this value to the existing values for this headerName.      *       * @param headerName the name of this header      * @param headerValue the value for this header      */
DECL|method|addHeader (String headerName, String headerValue)
name|void
name|addHeader
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|headerValue
parameter_list|)
function_decl|;
comment|/**      * Remove all headers with this name.      *       * @param headerName the name of this header      */
DECL|method|removeHeader (String headerName)
name|void
name|removeHeader
parameter_list|(
name|String
name|headerName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

