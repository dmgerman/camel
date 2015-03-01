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
comment|/**  * Represents an annotated Camel<a href="http://camel.apache.org/endpoint.html">Endpoint</a>  * which can have its properties (and the properties on its consumer) injected from the  * Camel URI path and its query parameters  */
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
name|TYPE
block|}
argument_list|)
DECL|annotation|UriEndpoint
specifier|public
annotation_defn|@interface
name|UriEndpoint
block|{
comment|/**      * Represents the URI scheme name of this endpoint.      *<p/>      * Multiple scheme names can be defined as a comma separated value.      * For example to associate<tt>http</tt> and<tt>https</tt> to the same endpoint implementation.      */
DECL|method|scheme ()
name|String
name|scheme
parameter_list|()
function_decl|;
comment|/**      * Represent the URI syntax the endpoint must use.      *<p/>      * The syntax follows the patterns such as:      *<ul>      *<li>scheme:host:port</li>      *<li>scheme:host:port/path</li>      *<li>scheme:path</li>      *<li>scheme:path/path2</li>      *</ul>      * Where each path maps to the name of the endpoint {@link org.apache.camel.spi.UriPath} option.      * The query parameters is implied and should not be included in the syntax.      *<p/>      * Some examples:      *<ul>      *<li>file:directoryName</li>      *<li>ftp:host:port/directoryName</li>      *<li>jms:destinationType:destinationName</li>      *</ul>      */
DECL|method|syntax ()
name|String
name|syntax
parameter_list|()
function_decl|;
comment|/**      * Represents the consumer class which is injected and created by consumers      */
DECL|method|consumerClass ()
DECL|field|Object.class
name|Class
argument_list|<
name|?
argument_list|>
name|consumerClass
parameter_list|()
default|default
name|Object
operator|.
name|class
function_decl|;
comment|/**      * The configuration parameter name prefix used on parameter names to separate the endpoint      * properties from the consumer properties      */
DECL|method|consumerPrefix ()
name|String
name|consumerPrefix
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * To associate this endpoint with label(s).      *<p/>      * Multiple labels can be defined as a comma separated value.      *<p/>      * The labels is intended for grouping the endpoints, such as<tt>core</tt>,<tt>file</tt>,<tt>messaging</tt>,<tt>database</tt>, etc.      */
DECL|method|label ()
name|String
name|label
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Whether this endpoint can only be used as a producer.      *<p/>      * By default its assumed the endpoint can be used as both consumer and producer.      */
DECL|method|producerOnly ()
DECL|field|false
name|boolean
name|producerOnly
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether this endpoint can only be used as a consumer.      *<p/>      * By default its assumed the endpoint can be used as both consumer and producer.      */
DECL|method|consumerOnly ()
DECL|field|false
name|boolean
name|consumerOnly
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

