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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_comment
comment|/**  * A consumer of a batch of message exchanges from an {@link Endpoint}  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|BatchConsumer
specifier|public
interface|interface
name|BatchConsumer
extends|extends
name|Consumer
block|{
comment|/**      * Processes the list of {@link org.apache.camel.Exchange} in a batch.      *<p/>      * Each message exchange will be processed individually but the batch      * consumer will add properties with the current index and total in the batch.      *      * @param exchanges list of exchanges in this batch      * @throws Exception if an internal processing error has occurred.      */
DECL|method|processBatch (Queue exchanges)
name|void
name|processBatch
parameter_list|(
name|Queue
name|exchanges
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

