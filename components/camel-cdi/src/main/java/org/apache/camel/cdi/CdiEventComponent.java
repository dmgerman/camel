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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Endpoint
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Named
argument_list|(
literal|"cdi-event"
argument_list|)
annotation|@
name|ApplicationScoped
DECL|class|CdiEventComponent
comment|/* package-private */
class|class
name|CdiEventComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
comment|/* The CDI event endpoint URI follows the format hereafter:                  cdi-event://PayloadType<T1,...,Tn>[?qualifiers=QualifierType1[,...[,QualifierTypeN]...]]          with the authority PayloadType (respectively the QualifierType) being the URI escaped fully         qualified name of the payload (respectively qualifier) raw type followed by the type parameters         section delimited by angle brackets for payload parameterized type.          Which leads to unfriendly URIs, e.g.:          cdi-event://org.apache.camel.cdi.se.pojo.EventPayload%3Cjava.lang.Integer%3E?qualifiers=org.apache.camel.cdi.se.qualifier.FooQualifier%2Corg.apache.camel.cdi.se.qualifier.BarQualifier          From the conceptual standpoint, that shows the high impedance between the typesafe nature of CDI         and the dynamic nature of the Camel component model.          From the implementation standpoint, that would prevent efficient binding between the endpoint         instances and observer methods as the CDI container doesn't have any ways of discovering the         Camel context model during the deployment phase.         */
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Creating CDI event endpoint isn't supported. Use @Inject "
operator|+
name|CdiEventEndpoint
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" instead"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

