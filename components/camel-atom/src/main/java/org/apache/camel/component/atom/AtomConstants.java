begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
package|;
end_package

begin_comment
comment|/**  * Atom constants  */
end_comment

begin_class
DECL|class|AtomConstants
specifier|public
specifier|final
class|class
name|AtomConstants
block|{
comment|/**      * Header key for the {@link org.apache.abdera.model.Feed} object is stored on the in message on the exchange.      */
DECL|field|ATOM_FEED
specifier|public
specifier|static
specifier|final
name|String
name|ATOM_FEED
init|=
literal|"CamelAtomFeed"
decl_stmt|;
DECL|method|AtomConstants ()
specifier|private
name|AtomConstants
parameter_list|()
block|{
comment|// utility class
block|}
block|}
end_class

end_unit

