begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Interface to allow plug-able implementation to filter header to and from Camel message.  */
end_comment

begin_interface
DECL|interface|HeaderFilterStrategy
specifier|public
interface|interface
name|HeaderFilterStrategy
block|{
comment|/**      * The direction is either<tt>IN</tt> or<tt>OUT</tt>.      */
DECL|enum|Direction
enum|enum
name|Direction
block|{
DECL|enumConstant|IN
DECL|enumConstant|OUT
name|IN
block|,
name|OUT
block|}
comment|/**      * Applies filtering logic to Camel Message header that is      * going to be copied to target message such as CXF and JMS message.      *<p/>      * It returns<tt>true</tt> if the filtering logic return a match.      * Otherwise, it returns<tt>false</tt>.      * A match means the header should be excluded.      *      * @param headerName  the header name      * @param headerValue the header value      * @param exchange    the context to perform filtering      * @return<tt>true</tt> if this header should be filtered out.      */
DECL|method|applyFilterToCamelHeaders (String headerName, Object headerValue, Exchange exchange)
name|boolean
name|applyFilterToCamelHeaders
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Applies filtering logic to an external message header such      * as CXF and JMS message that is going to be copied to Camel      * message header.      *<p/>      * It returns<tt>true</tt> if the filtering logic return a match.      * Otherwise, it returns<tt>false</tt>.      * A match means the header should be excluded.      *      * @param headerName  the header name      * @param headerValue the header value      * @param exchange    the context to perform filtering      * @return<tt>true</tt> if this header should be filtered out.      */
DECL|method|applyFilterToExternalHeaders (String headerName, Object headerValue, Exchange exchange)
name|boolean
name|applyFilterToExternalHeaders
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

