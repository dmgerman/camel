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

begin_comment
comment|/**  * Format allows to format object to and from string received using format or  * parse method  */
end_comment

begin_interface
DECL|interface|Format
specifier|public
interface|interface
name|Format
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Formats the object into a String      *       * @param object the object      * @return formatted as a String      * @throws Exception can be thrown      */
DECL|method|format (T object)
name|String
name|format
parameter_list|(
name|T
name|object
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Parses a String into an object      *       * @param string the string      * @return T the object      * @throws Exception can be thrown      */
DECL|method|parse (String string)
name|T
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

