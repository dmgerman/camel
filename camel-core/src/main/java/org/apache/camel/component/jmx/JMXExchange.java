begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
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
name|Exchange
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
name|ExchangePattern
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
import|;
end_import

begin_comment
comment|/**  * A {@link Exchange} for a jmx notification  *   * @version $Revision: 520985 $  */
end_comment

begin_class
DECL|class|JMXExchange
specifier|public
class|class
name|JMXExchange
extends|extends
name|DefaultExchange
block|{
comment|/**      * Constructor      *       * @param camelContext      * @param pattern      */
DECL|method|JMXExchange (CamelContext camelContext, ExchangePattern pattern, Notification notification)
specifier|public
name|JMXExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Notification
name|notification
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|JMXMessage
argument_list|(
name|notification
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

