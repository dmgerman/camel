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
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Indicates that this method is to be used as a   *<a href="http://camel.apache.org/recipient-list.html">Dynamic Recipient List</a> routing the incoming message  * to one or more endpoints.  *  * When a message {@link org.apache.camel.Exchange} is received from an {@link org.apache.camel.Endpoint} then the  *<a href="http://camel.apache.org/bean-integration.html">Bean Integration</a>  * mechanism is used to map the incoming {@link org.apache.camel.Message} to the method parameters.  *  * The return value of the method is then converted to either a {@link java.util.Collection} or array of objects where each  * element is converted to an {@link Endpoint} or a {@link String}, or if it is not a collection/array then it is converted  * to an {@link Endpoint} or {@link String}.  *  * Then for each endpoint or URI the message is forwarded a separate copy.  *  * @version   */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|CONSTRUCTOR
block|}
argument_list|)
DECL|annotation|RecipientList
specifier|public
annotation_defn|@interface
name|RecipientList
block|{
DECL|method|context ()
name|String
name|context
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|delimiter ()
name|String
name|delimiter
parameter_list|()
default|default
literal|","
function_decl|;
DECL|method|parallelProcessing ()
DECL|field|false
name|boolean
name|parallelProcessing
parameter_list|()
default|default
literal|false
function_decl|;
DECL|method|stopOnException ()
DECL|field|false
name|boolean
name|stopOnException
parameter_list|()
default|default
literal|false
function_decl|;
DECL|method|streaming ()
DECL|field|false
name|boolean
name|streaming
parameter_list|()
default|default
literal|false
function_decl|;
DECL|method|ignoreInvalidEndpoints ()
DECL|field|false
name|boolean
name|ignoreInvalidEndpoints
parameter_list|()
default|default
literal|false
function_decl|;
DECL|method|strategyRef ()
name|String
name|strategyRef
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|executorServiceRef ()
name|String
name|executorServiceRef
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|timeout ()
name|long
name|timeout
parameter_list|()
default|default
literal|0
function_decl|;
DECL|method|onPrepareRef ()
name|String
name|onPrepareRef
parameter_list|()
default|default
literal|""
function_decl|;
annotation|@
name|Deprecated
DECL|method|shareUnitOfWork ()
DECL|field|false
name|boolean
name|shareUnitOfWork
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

