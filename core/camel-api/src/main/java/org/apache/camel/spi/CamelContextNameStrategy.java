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
comment|/**  * Strategy for assigning name to a {@link org.apache.camel.CamelContext}.  *  * @see ManagementNameStrategy  */
end_comment

begin_interface
DECL|interface|CamelContextNameStrategy
specifier|public
interface|interface
name|CamelContextNameStrategy
block|{
comment|/**      * Gets the name      *<p/>      * The {@link #isFixedName()} determines if the name can be re-calculated such as when using a counter,      * or the name is always fixed.      *      * @return the name.      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Gets the next calculated name, if this strategy is not using fixed names.      *<p/>      * The {@link #isFixedName()} determines if the name can be re-calculated such as when using a counter,      * or the name is always fixed.      *      * @return the next name      */
DECL|method|getNextName ()
name|String
name|getNextName
parameter_list|()
function_decl|;
comment|/**      * Whether the name will be fixed, or allow re-calculation such as by using an unique counter.      *       * @return<tt>true</tt> for fixed names,<tt>false</tt> for names which can re-calculated      */
DECL|method|isFixedName ()
name|boolean
name|isFixedName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

