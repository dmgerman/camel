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
name|IQuery
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
name|IBaseBundle
import|;
end_import

begin_comment
comment|/**  * API to search for resources matching a given set of criteria. Searching is a very powerful  * feature in FHIR with many features for specifying exactly what should be searched for  * and how it should be returned. See the<a href="http://www.hl7.org/fhir/search.html">specification on search</a>  * for more information.  */
end_comment

begin_class
DECL|class|FhirSearch
specifier|public
class|class
name|FhirSearch
block|{
DECL|field|client
specifier|private
specifier|final
name|IGenericClient
name|client
decl_stmt|;
DECL|method|FhirSearch (IGenericClient client)
specifier|public
name|FhirSearch
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
comment|/**      * Perform a search directly by URL.      *      * @param url The URL to search for. Note that this URL may be complete (e.g. "http://example.com/base/Patient?name=foo") in which case the client's base URL will be ignored. Or it can be relative      *            (e.g. "Patient?name=foo") in which case the client's base URL will be used.      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link IBaseBundle}      */
DECL|method|searchByUrl (String url, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|IBaseBundle
name|searchByUrl
parameter_list|(
name|String
name|url
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
name|IQuery
argument_list|<
name|IBaseBundle
argument_list|>
name|query
init|=
name|client
operator|.
name|search
argument_list|()
operator|.
name|byUrl
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
name|query
operator|.
name|execute
argument_list|()
return|;
block|}
block|}
end_class

end_unit

