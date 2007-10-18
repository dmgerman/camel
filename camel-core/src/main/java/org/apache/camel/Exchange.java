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
name|Map
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
name|spi
operator|.
name|UnitOfWork
import|;
end_import

begin_comment
comment|/**  * The base message exchange interface providing access to the request, response  * and fault {@link Message} instances. Different providers such as JMS, JBI,  * CXF and HTTP can provide their own derived API to expose the underlying  * transport semantics to avoid the leaky abstractions of generic APIs.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Exchange
specifier|public
interface|interface
name|Exchange
block|{
comment|/**      * Returns the {@link ExchangePattern} (MEP) of this exchange.      *      * @return the message exchange pattern of this exchange      */
DECL|method|getPattern ()
name|ExchangePattern
name|getPattern
parameter_list|()
function_decl|;
comment|/**      * Returns a property associated with this exchange by name      *      * @param name the name of the property      * @return the value of the given header or null if there is no property for      *         the given name      */
DECL|method|getProperty (String name)
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns a property associated with this exchange by name and specifying      * the type required      *      * @param name the name of the property      * @param type the type of the property      * @return the value of the given header or null if there is no property for      *         the given name or null if it cannot be converted to the given      *         type      */
DECL|method|getProperty (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets a property on the exchange      *      * @param name  of the property      * @param value to associate with the name      */
DECL|method|setProperty (String name, Object value)
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Removes the given property on the exchange      *      * @param name of the property      * @return the old value of the property      */
DECL|method|removeProperty (String name)
name|Object
name|removeProperty
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns all of the properties associated with the exchange      *      * @return all the headers in a Map      */
DECL|method|getProperties ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
function_decl|;
comment|/**      * Returns the inbound request message      *      * @return the message      */
DECL|method|getIn ()
name|Message
name|getIn
parameter_list|()
function_decl|;
comment|/**      * Sets the inbound message instance      *      * @param in the inbound message      */
DECL|method|setIn (Message in)
name|void
name|setIn
parameter_list|(
name|Message
name|in
parameter_list|)
function_decl|;
comment|/**      * Returns the outbound message, lazily creating one if one has not already      * been associated with this exchange. If you want to inspect this property      * but not force lazy creation then invoke the {@link #getOut(boolean)}      * method passing in null      *      * @return the response      */
DECL|method|getOut ()
name|Message
name|getOut
parameter_list|()
function_decl|;
comment|/**      * Returns the outbound message; optionally lazily creating one if one has      * not been associated with this exchange      *      * @return the response      */
DECL|method|getOut (boolean lazyCreate)
name|Message
name|getOut
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
function_decl|;
comment|/**      * Sets the outbound message      *      * @param out the outbound message      */
DECL|method|setOut (Message out)
name|void
name|setOut
parameter_list|(
name|Message
name|out
parameter_list|)
function_decl|;
comment|/**      * Returns the fault message      *      * @return the fault      */
DECL|method|getFault ()
name|Message
name|getFault
parameter_list|()
function_decl|;
comment|/**      * Returns the fault message; optionally lazily creating one if one has      * not been associated with this exchange      *      * @return the response      */
DECL|method|getFault (boolean lazyCreate)
name|Message
name|getFault
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
function_decl|;
comment|/**      * Returns the exception associated with this exchange      *      * @return the exception (or null if no faults)      */
DECL|method|getException ()
name|Throwable
name|getException
parameter_list|()
function_decl|;
comment|/**      * Sets the exception associated with this exchange      *      * @param e      */
DECL|method|setException (Throwable e)
name|void
name|setException
parameter_list|(
name|Throwable
name|e
parameter_list|)
function_decl|;
comment|/**      * Returns true if this exchange failed due to either an exception or fault      *      * @return true if this exchange failed due to either an exception or fault      * @see Exchange#getException()      * @see Exchange#getFault()      */
DECL|method|isFailed ()
name|boolean
name|isFailed
parameter_list|()
function_decl|;
comment|/**      * Returns the container so that a processor can resolve endpoints from URIs      *      * @return the container which owns this exchange      */
DECL|method|getContext ()
name|CamelContext
name|getContext
parameter_list|()
function_decl|;
comment|/**      * Creates a new exchange instance with empty messages, headers and properties      *      * @return      */
DECL|method|newInstance ()
name|Exchange
name|newInstance
parameter_list|()
function_decl|;
comment|/**      * Creates a copy of the current message exchange so that it can be      * forwarded to another destination      */
DECL|method|copy ()
name|Exchange
name|copy
parameter_list|()
function_decl|;
comment|/**      * Copies the data into this exchange from the given exchange      *<p/>      * #param source is the source from which headers and messages will be      * copied      */
DECL|method|copyFrom (Exchange source)
name|void
name|copyFrom
parameter_list|(
name|Exchange
name|source
parameter_list|)
function_decl|;
comment|/**      * Returns the unit of work that this exchange belongs to; which may map to      * zero, one or more physical transactions      */
DECL|method|getUnitOfWork ()
name|UnitOfWork
name|getUnitOfWork
parameter_list|()
function_decl|;
comment|/**      * Sets the unit of work that this exchange belongs to; which may map to      * zero, one or more physical transactions      */
DECL|method|setUnitOfWork (UnitOfWork unitOfWork)
name|void
name|setUnitOfWork
parameter_list|(
name|UnitOfWork
name|unitOfWork
parameter_list|)
function_decl|;
comment|/**      * Returns the exchange id      *      * @return the unique id of the exchange      */
DECL|method|getExchangeId ()
name|String
name|getExchangeId
parameter_list|()
function_decl|;
comment|/**      * Set the exchange id      *      * @param id      */
DECL|method|setExchangeId (String id)
name|void
name|setExchangeId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

