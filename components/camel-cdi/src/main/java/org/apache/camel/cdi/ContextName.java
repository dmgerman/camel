begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Qualifier
import|;
end_import

begin_comment
comment|/**  * CDI qualifier to be used for multi Camel contexts CDI deployment.  * {@code CamelContext} beans can be annotated with the {@code @ContextName} qualifier  * so that the Camel context is named accordingly, e.g.:  *  *<pre><code>  * {@literal @}ApplicationScoped  * {@literal @}ContextName("foo")  * public class FooCamelContext extends DefaultCamelContext {  * }  *</code></pre>  *  * Camel resources like route builders, endpoints and producer templates can be annotated with  * the {@code @ContextName} qualifier as well so that they are associated with the  * corresponding Camel context, e.g.:  *  *<pre><code>  * {@literal @}ContextName("foo")  * public class FooRouteBuilder extends RouteBuilder {  *  *     {@literal @}Override  *     public void configure() {  *         from("direct:bar").to("mock:bar");  *     }  * }  *  * {@literal @}Inject  * {@literal @}ContextName("foo")  * {@literal @}Uri("direct:bar")  * ProducerTemplate barProducer;  *  * {@literal @}Inject  * {@literal @}ContextName("foo")  * {@literal @}Uri("mock:bar")  * MockEndpoint barMockEndpoint;  *</code></pre>  *  * @see org.apache.camel.CamelContext  *  */
end_comment

begin_annotation_defn
annotation|@
name|Qualifier
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
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|PARAMETER
block|}
argument_list|)
DECL|annotation|ContextName
specifier|public
annotation_defn|@interface
name|ContextName
block|{
comment|/**      * Returns the name of the Camel context.      */
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
DECL|class|Literal
specifier|final
class|class
name|Literal
extends|extends
name|AnnotationLiteral
argument_list|<
name|ContextName
argument_list|>
implements|implements
name|ContextName
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|Literal (String name)
specifier|public
name|Literal
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
block|}
end_annotation_defn

end_unit

