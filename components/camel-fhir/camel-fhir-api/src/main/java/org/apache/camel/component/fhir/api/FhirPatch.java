begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
operator|.
name|api
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
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|api
operator|.
name|MethodOutcome
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|api
operator|.
name|PreferReturnEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|IGenericClient
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|gclient
operator|.
name|IPatchExecutable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|instance
operator|.
name|model
operator|.
name|api
operator|.
name|IIdType
import|;
end_import

begin_comment
comment|/**  * API for the "patch" operation, which performs a logical patch on a server resource  */
end_comment

begin_class
DECL|class|FhirPatch
specifier|public
class|class
name|FhirPatch
block|{
DECL|field|client
specifier|private
specifier|final
name|IGenericClient
name|client
decl_stmt|;
DECL|method|FhirPatch (IGenericClient client)
specifier|public
name|FhirPatch
parameter_list|(
name|IGenericClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
comment|/**      * Specifies that the update should be performed as a conditional create      * against a given search URL.      *      * @param url The search URL to use. The format of this URL should be of the form<code>[ResourceType]?[Parameters]</code>,      *            for example:<code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>      * @param patchBody The body of the patch document serialized in either XML or JSON which conforms to      *                  http://jsonpatch.com/ or http://tools.ietf.org/html/rfc5261      * @param preferReturn Add a<code>Prefer</code> header to the request, which requests that the server include      *                     or suppress the resource body as a part of the result. If a resource is returned by the server      *                     it will be parsed an accessible to the client via {@link MethodOutcome#getResource()}      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link MethodOutcome}      */
DECL|method|patchByUrl (String patchBody, String url, PreferReturnEnum preferReturn, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|MethodOutcome
name|patchByUrl
parameter_list|(
name|String
name|patchBody
parameter_list|,
name|String
name|url
parameter_list|,
name|PreferReturnEnum
name|preferReturn
parameter_list|,
name|Map
argument_list|<
name|ExtraParameters
argument_list|,
name|Object
argument_list|>
name|extraParameters
parameter_list|)
block|{
name|IPatchExecutable
name|patchExecutable
init|=
name|client
operator|.
name|patch
argument_list|()
operator|.
name|withBody
argument_list|(
name|patchBody
argument_list|)
operator|.
name|conditionalByUrl
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferReturn
operator|!=
literal|null
condition|)
block|{
name|patchExecutable
operator|.
name|prefer
argument_list|(
name|preferReturn
argument_list|)
expr_stmt|;
block|}
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|patchExecutable
argument_list|)
expr_stmt|;
return|return
name|patchExecutable
operator|.
name|execute
argument_list|()
return|;
block|}
comment|/**      * Applies the patch to the given resource ID      *      * @param patchBody The body of the patch document serialized in either XML or JSON which conforms to      *                  http://jsonpatch.com/ or http://tools.ietf.org/html/rfc5261      * @param id The resource ID to patch      * @param preferReturn Add a<code>Prefer</code> header to the request, which requests that the server include      *                     or suppress the resource body as a part of the result. If a resource is returned by the server      *                     it will be parsed an accessible to the client via {@link MethodOutcome#getResource()}      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link MethodOutcome}      */
DECL|method|patchById (String patchBody, IIdType id, PreferReturnEnum preferReturn, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|MethodOutcome
name|patchById
parameter_list|(
name|String
name|patchBody
parameter_list|,
name|IIdType
name|id
parameter_list|,
name|PreferReturnEnum
name|preferReturn
parameter_list|,
name|Map
argument_list|<
name|ExtraParameters
argument_list|,
name|Object
argument_list|>
name|extraParameters
parameter_list|)
block|{
name|IPatchExecutable
name|patchExecutable
init|=
name|client
operator|.
name|patch
argument_list|()
operator|.
name|withBody
argument_list|(
name|patchBody
argument_list|)
operator|.
name|withId
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferReturn
operator|!=
literal|null
condition|)
block|{
name|patchExecutable
operator|.
name|prefer
argument_list|(
name|preferReturn
argument_list|)
expr_stmt|;
block|}
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|patchExecutable
argument_list|)
expr_stmt|;
return|return
name|patchExecutable
operator|.
name|execute
argument_list|()
return|;
block|}
comment|/**      * Applies the patch to the given resource ID      *      * @param patchBody The body of the patch document serialized in either XML or JSON which conforms to      *                  http://jsonpatch.com/ or http://tools.ietf.org/html/rfc5261      * @param stringId The resource ID to patch      * @param preferReturn Add a<code>Prefer</code> header to the request, which requests that the server include      *                     or suppress the resource body as a part of the result. If a resource is returned by the server      *                     it will be parsed an accessible to the client via {@link MethodOutcome#getResource()}      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link MethodOutcome}      */
DECL|method|patchById (String patchBody, String stringId, PreferReturnEnum preferReturn, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|MethodOutcome
name|patchById
parameter_list|(
name|String
name|patchBody
parameter_list|,
name|String
name|stringId
parameter_list|,
name|PreferReturnEnum
name|preferReturn
parameter_list|,
name|Map
argument_list|<
name|ExtraParameters
argument_list|,
name|Object
argument_list|>
name|extraParameters
parameter_list|)
block|{
name|IPatchExecutable
name|patchExecutable
init|=
name|client
operator|.
name|patch
argument_list|()
operator|.
name|withBody
argument_list|(
name|patchBody
argument_list|)
operator|.
name|withId
argument_list|(
name|stringId
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferReturn
operator|!=
literal|null
condition|)
block|{
name|patchExecutable
operator|.
name|prefer
argument_list|(
name|preferReturn
argument_list|)
expr_stmt|;
block|}
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|patchExecutable
argument_list|)
expr_stmt|;
return|return
name|patchExecutable
operator|.
name|execute
argument_list|()
return|;
block|}
block|}
end_class

end_unit

