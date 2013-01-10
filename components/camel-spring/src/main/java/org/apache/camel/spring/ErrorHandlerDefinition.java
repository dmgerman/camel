begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|IdentifiedType
import|;
end_import

begin_comment
comment|/**  * The&lt;errorHandler&gt; tag element.  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"errorHandler"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|ErrorHandlerDefinition
specifier|public
class|class
name|ErrorHandlerDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|ErrorHandlerType
name|type
init|=
name|ErrorHandlerType
operator|.
name|DefaultErrorHandler
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|deadLetterUri
specifier|private
name|String
name|deadLetterUri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|level
specifier|private
name|LoggingLevel
name|level
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rollbackLoggingLevel
specifier|private
name|LoggingLevel
name|rollbackLoggingLevel
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|logName
specifier|private
name|String
name|logName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useOriginalMessage
specifier|private
name|Boolean
name|useOriginalMessage
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|transactionTemplateRef
specifier|private
name|String
name|transactionTemplateRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|transactionManagerRef
specifier|private
name|String
name|transactionManagerRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onRedeliveryRef
specifier|private
name|String
name|onRedeliveryRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|retryWhileRef
specifier|private
name|String
name|retryWhileRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|redeliveryPolicyRef
specifier|private
name|String
name|redeliveryPolicyRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|redeliveryPolicy
specifier|private
name|CamelRedeliveryPolicyFactoryBean
name|redeliveryPolicy
decl_stmt|;
block|}
end_class

end_unit

