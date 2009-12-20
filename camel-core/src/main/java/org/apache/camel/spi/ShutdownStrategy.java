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
name|java
operator|.
name|util
operator|.
name|List
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
name|CamelContext
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
name|Consumer
import|;
end_import

begin_comment
comment|/**  * Pluggable shutdown strategy executed during shutdown of routes.  *<p/>  * Shutting down routes in a reliable and graceful manner is not a trivial task. Therefore Camel provides a pluggable  * strategy allowing 3rd party to use their own strategy if needed.  *<p/>  * The key problem is to stop the input consumers for the routes such that no new messages is coming into Camel.  * But at the same time still keep the routes running so the existing in flight exchanges can still be run to  * completion. On top of that there are some in memory components (such as SEDA) which may have pending messages  * on its in memory queue which we want to run to completion as well, otherwise they will get lost.  *<p/>  * Camel provides a default strategy which supports all that that can be used as inspiration for your own strategy.  *  * @version $Revision$  * @see org.apache.camel.spi.ShutdownAware  */
end_comment

begin_interface
DECL|interface|ShutdownStrategy
specifier|public
interface|interface
name|ShutdownStrategy
block|{
comment|/**      * Shutdown the routes      *      * @param context the camel context      * @param consumers the consumers for the routes, ordered by the order they was started      * @throws Exception is thrown if error shutting down the consumers, however its preferred to avoid this      */
DECL|method|shutdown (CamelContext context, List<Consumer> consumers)
name|void
name|shutdown
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|List
argument_list|<
name|Consumer
argument_list|>
name|consumers
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

