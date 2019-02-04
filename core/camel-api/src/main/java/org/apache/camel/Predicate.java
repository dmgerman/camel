begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Evaluates a binary<a  * href="http://camel.apache.org/predicate.html">predicate</a> on the  * message exchange to support things like<a  * href="http://camel.apache.org/scripting-languages.html">scripting  * languages</a>,<a href="http://camel.apache.org/xquery.html">XQuery</a>  * or<a href="http://camel.apache.org/sql.html">SQL</a> as well as  * any arbitrary Java expression.  */
end_comment

begin_interface
DECL|interface|Predicate
specifier|public
interface|interface
name|Predicate
block|{
comment|/**      * Evaluates the predicate on the message exchange and returns true if this      * exchange matches the predicate      *       * @param exchange the message exchange      * @return true if the predicate matches      */
DECL|method|matches (Exchange exchange)
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

