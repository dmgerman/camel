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
comment|/**  * Interface to be implemented by objects that should be orderable, such as with a {@link java.util.Collection}.  *  * @version   */
end_comment

begin_interface
DECL|interface|Ordered
specifier|public
interface|interface
name|Ordered
block|{
comment|/**      * The highest precedence      */
DECL|field|HIGHEST
name|int
name|HIGHEST
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
comment|/**      * The lowest precedence      */
DECL|field|LOWEST
name|int
name|LOWEST
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
comment|/**      * Gets the order.      *<p/>      * Use low numbers for higher priority. Normally the sorting will start from 0 and move upwards.      * So if you want to be last then use {@link Integer#MAX_VALUE} or eg {@link #LOWEST}.      *      * @return the order      */
DECL|method|getOrder ()
name|int
name|getOrder
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

