begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|AmazonSimpleDBClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|BatchDeleteAttributesRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|BatchPutAttributesRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|CreateDomainRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DeleteAttributesRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DeleteDomainRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DomainMetadataRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DomainMetadataResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|GetAttributesRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|GetAttributesResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|Item
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|ListDomainsRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|ListDomainsResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|NoSuchDomainException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|PutAttributesRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|SelectRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|SelectResult
import|;
end_import

begin_class
DECL|class|AmazonSDBClientMock
specifier|public
class|class
name|AmazonSDBClientMock
extends|extends
name|AmazonSimpleDBClient
block|{
DECL|field|batchDeleteAttributesRequest
specifier|protected
name|BatchDeleteAttributesRequest
name|batchDeleteAttributesRequest
decl_stmt|;
DECL|field|batchPutAttributesRequest
specifier|protected
name|BatchPutAttributesRequest
name|batchPutAttributesRequest
decl_stmt|;
DECL|field|createDomainRequest
specifier|protected
name|CreateDomainRequest
name|createDomainRequest
decl_stmt|;
DECL|field|deleteAttributesRequest
specifier|protected
name|DeleteAttributesRequest
name|deleteAttributesRequest
decl_stmt|;
DECL|field|deleteDomainRequest
specifier|protected
name|DeleteDomainRequest
name|deleteDomainRequest
decl_stmt|;
DECL|field|domainMetadataRequest
specifier|protected
name|DomainMetadataRequest
name|domainMetadataRequest
decl_stmt|;
DECL|field|getAttributesRequest
specifier|protected
name|GetAttributesRequest
name|getAttributesRequest
decl_stmt|;
DECL|field|listDomainsRequest
specifier|protected
name|ListDomainsRequest
name|listDomainsRequest
decl_stmt|;
DECL|field|putAttributesRequest
specifier|protected
name|PutAttributesRequest
name|putAttributesRequest
decl_stmt|;
DECL|field|selectRequest
specifier|protected
name|SelectRequest
name|selectRequest
decl_stmt|;
DECL|method|AmazonSDBClientMock ()
specifier|public
name|AmazonSDBClientMock
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|BasicAWSCredentials
argument_list|(
literal|"user"
argument_list|,
literal|"secret"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|batchDeleteAttributes (BatchDeleteAttributesRequest batchDeleteAttributesRequest)
specifier|public
name|void
name|batchDeleteAttributes
parameter_list|(
name|BatchDeleteAttributesRequest
name|batchDeleteAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|batchDeleteAttributesRequest
operator|=
name|batchDeleteAttributesRequest
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|batchPutAttributes (BatchPutAttributesRequest batchPutAttributesRequest)
specifier|public
name|void
name|batchPutAttributes
parameter_list|(
name|BatchPutAttributesRequest
name|batchPutAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|batchPutAttributesRequest
operator|=
name|batchPutAttributesRequest
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createDomain (CreateDomainRequest createDomainRequest)
specifier|public
name|void
name|createDomain
parameter_list|(
name|CreateDomainRequest
name|createDomainRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|createDomainRequest
operator|=
name|createDomainRequest
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deleteAttributes (DeleteAttributesRequest deleteAttributesRequest)
specifier|public
name|void
name|deleteAttributes
parameter_list|(
name|DeleteAttributesRequest
name|deleteAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|deleteAttributesRequest
operator|=
name|deleteAttributesRequest
expr_stmt|;
name|String
name|domainName
init|=
name|deleteAttributesRequest
operator|.
name|getDomainName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"MissingDomain"
operator|.
name|equals
argument_list|(
name|domainName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NoSuchDomainException
argument_list|(
name|domainName
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|deleteDomain (DeleteDomainRequest deleteDomainRequest)
specifier|public
name|void
name|deleteDomain
parameter_list|(
name|DeleteDomainRequest
name|deleteDomainRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|deleteDomainRequest
operator|=
name|deleteDomainRequest
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|domainMetadata (DomainMetadataRequest domainMetadataRequest)
specifier|public
name|DomainMetadataResult
name|domainMetadata
parameter_list|(
name|DomainMetadataRequest
name|domainMetadataRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|domainMetadataRequest
operator|=
name|domainMetadataRequest
expr_stmt|;
if|if
condition|(
literal|"NonExistingDomain"
operator|.
name|equals
argument_list|(
name|domainMetadataRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NoSuchDomainException
argument_list|(
literal|"Domain 'NonExistingDomain' doesn't exist."
argument_list|)
throw|;
block|}
name|DomainMetadataResult
name|result
init|=
operator|new
name|DomainMetadataResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|setTimestamp
argument_list|(
operator|new
name|Integer
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setItemCount
argument_list|(
operator|new
name|Integer
argument_list|(
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAttributeNameCount
argument_list|(
operator|new
name|Integer
argument_list|(
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAttributeValueCount
argument_list|(
operator|new
name|Integer
argument_list|(
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAttributeNamesSizeBytes
argument_list|(
operator|new
name|Long
argument_list|(
literal|1000000
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAttributeValuesSizeBytes
argument_list|(
operator|new
name|Long
argument_list|(
literal|2000000
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setItemNamesSizeBytes
argument_list|(
operator|new
name|Long
argument_list|(
literal|3000000
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|getAttributes (GetAttributesRequest getAttributesRequest)
specifier|public
name|GetAttributesResult
name|getAttributes
parameter_list|(
name|GetAttributesRequest
name|getAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|getAttributesRequest
operator|=
name|getAttributesRequest
expr_stmt|;
return|return
operator|new
name|GetAttributesResult
argument_list|()
operator|.
name|withAttributes
argument_list|(
operator|new
name|Attribute
argument_list|(
literal|"AttributeOne"
argument_list|,
literal|"Value One"
argument_list|)
argument_list|)
operator|.
name|withAttributes
argument_list|(
operator|new
name|Attribute
argument_list|(
literal|"AttributeTwo"
argument_list|,
literal|"Value Two"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|listDomains (ListDomainsRequest listDomainsRequest)
specifier|public
name|ListDomainsResult
name|listDomains
parameter_list|(
name|ListDomainsRequest
name|listDomainsRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|listDomainsRequest
operator|=
name|listDomainsRequest
expr_stmt|;
name|ListDomainsResult
name|result
init|=
operator|new
name|ListDomainsResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|getDomainNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"DOMAIN1"
argument_list|)
expr_stmt|;
name|result
operator|.
name|getDomainNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"DOMAIN2"
argument_list|)
expr_stmt|;
name|result
operator|.
name|setNextToken
argument_list|(
literal|"TOKEN2"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|putAttributes (PutAttributesRequest putAttributesRequest)
specifier|public
name|void
name|putAttributes
parameter_list|(
name|PutAttributesRequest
name|putAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|putAttributesRequest
operator|=
name|putAttributesRequest
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|select (SelectRequest selectRequest)
specifier|public
name|SelectResult
name|select
parameter_list|(
name|SelectRequest
name|selectRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|selectRequest
operator|=
name|selectRequest
expr_stmt|;
name|SelectResult
name|result
init|=
operator|new
name|SelectResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|setNextToken
argument_list|(
literal|"TOKEN2"
argument_list|)
expr_stmt|;
name|result
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Item
argument_list|(
literal|"ITEM1"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Item
argument_list|(
literal|"ITEM2"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

