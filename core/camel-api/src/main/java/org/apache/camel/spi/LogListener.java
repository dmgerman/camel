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
name|LoggingLevel
import|;
end_import

begin_comment
comment|/**  * An event listener SPI for logging. Listeners are registered into {@link org.apache.camel.processor.LogProcessor} and  * {@link org.apache.camel.processor.CamelLogProcessor} so that the logging events are delivered for both of Log Component and Log EIP.  */
end_comment

begin_interface
DECL|interface|LogListener
specifier|public
interface|interface
name|LogListener
block|{
comment|/**      * Invoked right before Log component or Log EIP logs.      * Note that {@link CamelLogger} holds the {@link LoggingLevel} and {@link org.slf4j.Marker}.      * The listener can check {@link CamelLogger#getLevel()} to see in which log level      * this is going to be logged.      *       * @param exchange camel exchange      * @param camelLogger {@link CamelLogger}      * @param message log message      * @return log message, possibly enriched by the listener      */
DECL|method|onLog (Exchange exchange, CamelLogger camelLogger, String message)
name|String
name|onLog
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CamelLogger
name|camelLogger
parameter_list|,
name|String
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

