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

begin_comment
comment|/**  * An interface to represent an object which can make use of  * injected {@link HeaderFilterStrategy}.  *   * @since 1.5  */
end_comment

begin_interface
DECL|interface|HeaderFilterStrategyAware
specifier|public
interface|interface
name|HeaderFilterStrategyAware
block|{
comment|/**      * Gets the header filter strategy used      *      * @return the strategy      */
DECL|method|getHeaderFilterStrategy ()
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the header filter strategy to use      *      * @param strategy the strategy      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

