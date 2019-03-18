begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
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
comment|/**  * Indicates one or more resources representing<a href="http://camel.apache.org/http://camel.apache.org/xml-configuration.html">Camel XML configuration</a>  * to import. Resources are currently loaded from the classpath.<p>  *  * {@code CamelContext} elements and other Camel primitives are automatically  * deployed as CDI beans during the container bootstrap so that they become  * available for injection at runtime. If such an element has an explicit  * {@code id} attribute set, the corresponding CDI bean is qualified with the  * {@code @Named} qualifier, e.g., given the following Camel XML configuration:  *  *<pre>{@code  *<camelContext id="foo">  *<endpoint id="bar" uri="seda:inbound">  *<property key="queue" value="#queue"/>  *<property key="concurrentConsumers" value="10"/>  *</endpoint>  *<camelContext/>  * }</pre>  *  * Corresponding CDI beans are automatically deployed and can be injected, e.g.:  *  *<pre><code>  * {@literal @}Inject  * {@literal @}ContextName("foo")  *  CamelContext context;  *</code></pre>  *  *<pre><code>  * {@literal @}Inject  * {@literal @}Named("bar")  *  Endpoint endpoint;  *</code></pre>  *  * Note that {@code CamelContext} beans are automatically qualified with both  * the {@code Named} and {@code ContextName} qualifiers. If the imported  * {@code CamelContext} element doesn't have an {@code id} attribute, the  * corresponding bean is deployed with the built-in {@code Default} qualifier.<p>  *  * Conversely, CDI beans deployed in the application can be referred to from  * the Camel XML configuration, usually using the {@code ref} attribute, e.g.,  * given the following bean declared:  *  *<pre><code>  * {@literal @}Produces  * {@literal @}Named("baz")  *  Processor processor = exchange{@literal ->} exchange.getIn().setHeader("qux", "quux");  *</code></pre>  *  * A reference to that bean can be declared in the imported Camel XML configuration,  * e.g.:  *  *<pre>{@code  *<camelContext id="foo">  *<route>  *<from uri="..."/>  *<process ref="baz"/>  *</route>  *<camelContext/>  * }</pre>  *  * @since 2.18.0  */
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
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
DECL|annotation|ImportResource
specifier|public
annotation_defn|@interface
name|ImportResource
block|{
comment|/**      * Resource locations from which to import Camel XML configuration.      *      * @return the locations of the resources to import      */
DECL|method|value ()
name|String
index|[]
name|value
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

end_unit

