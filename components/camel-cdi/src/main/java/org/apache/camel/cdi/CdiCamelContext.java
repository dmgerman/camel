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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_comment
comment|/**  * CDI {@link org.apache.camel.CamelContext} class that can be extended  * to declare custom Camel context beans. Camel CDI is capable of managing  * any bean that implements {@code org.apache.camel.CamelContext},  * so that directly extending {@link org.apache.camel.impl.DefaultCamelContext}  * is an option to avoid having to depend on Camel CDI specific API, e.g.:  *  *<pre><code>  * {@literal @}ApplicationScoped  * {@literal @}ContextName("foo")  * public class FooCamelContext extends DefaultCamelContext {  * }  *</code></pre>  */
end_comment

begin_class
annotation|@
name|Vetoed
DECL|class|CdiCamelContext
specifier|public
class|class
name|CdiCamelContext
extends|extends
name|DefaultCamelContext
block|{  }
end_class

end_unit

