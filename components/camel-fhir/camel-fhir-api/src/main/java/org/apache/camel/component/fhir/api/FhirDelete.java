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
name|IDeleteTyped
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
name|IBaseOperationOutcome
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
name|IBaseResource
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
comment|/**  * API for the "delete" operation, which performs a logical delete on a server resource.  */
end_comment

begin_class
DECL|class|FhirDelete
specifier|public
class|class
name|FhirDelete
block|{
DECL|field|client
specifier|private
specifier|final
name|IGenericClient
name|client
decl_stmt|;
DECL|method|FhirDelete (IGenericClient client)
specifier|public
name|FhirDelete
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
comment|/**      * Deletes the given resource      *      * @param resource the {@link IBaseResource} to delete      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link IBaseOperationOutcome}      */
DECL|method|resource (IBaseResource resource, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|IBaseOperationOutcome
name|resource
parameter_list|(
name|IBaseResource
name|resource
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
name|IDeleteTyped
name|deleteTyped
init|=
name|client
operator|.
name|delete
argument_list|()
operator|.
name|resource
argument_list|(
name|resource
argument_list|)
decl_stmt|;
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|deleteTyped
argument_list|)
expr_stmt|;
return|return
name|deleteTyped
operator|.
name|execute
argument_list|()
return|;
block|}
comment|/**      * * Deletes the given resource by {@link IIdType}      *      * @param id the {@link IIdType} referencing the resource      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link IBaseOperationOutcome}      */
DECL|method|resourceById (IIdType id, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|IBaseOperationOutcome
name|resourceById
parameter_list|(
name|IIdType
name|id
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
name|IDeleteTyped
name|deleteTyped
init|=
name|client
operator|.
name|delete
argument_list|()
operator|.
name|resourceById
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|deleteTyped
argument_list|)
expr_stmt|;
return|return
name|deleteTyped
operator|.
name|execute
argument_list|()
return|;
block|}
comment|/**      * Deletes the resource by resource type e.g "Patient" and it's id      * @param type the resource type e.g "Patient"      * @param stringId it's id      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link IBaseOperationOutcome}      */
DECL|method|resourceById (String type, String stringId, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|IBaseOperationOutcome
name|resourceById
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|stringId
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
name|IDeleteTyped
name|deleteTyped
init|=
name|client
operator|.
name|delete
argument_list|()
operator|.
name|resourceById
argument_list|(
name|type
argument_list|,
name|stringId
argument_list|)
decl_stmt|;
name|ExtraParameters
operator|.
name|process
argument_list|(
name|extraParameters
argument_list|,
name|deleteTyped
argument_list|)
expr_stmt|;
return|return
name|deleteTyped
operator|.
name|execute
argument_list|()
return|;
block|}
comment|/**      * Specifies that the delete should be performed as a conditional delete against a given search URL.      * @param url The search URL to use. The format of this URL should be of the form      *<code>[ResourceType]?[Parameters]</code>,      *            for example:<code>Patient?name=Smith&amp;identifier=13.2.4.11.4%7C847366</code>      * @param extraParameters see {@link ExtraParameters} for a full list of parameters that can be passed, may be NULL      * @return the {@link IBaseOperationOutcome}      */
DECL|method|resourceConditionalByUrl (String url, Map<ExtraParameters, Object> extraParameters)
specifier|public
name|IBaseOperationOutcome
name|resourceConditionalByUrl
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
name|IDeleteTyped
name|deleteTyped
init|=
name|client
operator|.
name|delete
argument_list|()
operator|.
name|resourceConditionalByUrl
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
name|deleteTyped
argument_list|)
expr_stmt|;
return|return
name|deleteTyped
operator|.
name|execute
argument_list|()
return|;
block|}
block|}
end_class

end_unit

