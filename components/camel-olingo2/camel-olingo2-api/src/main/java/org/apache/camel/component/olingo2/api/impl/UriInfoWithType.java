begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2.api.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
operator|.
name|api
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|commons
operator|.
name|InlineCount
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmEntityContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmEntitySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmFunctionImport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmLiteral
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmMultiplicity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmTypeKind
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|exception
operator|.
name|ODataApplicationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|KeyPredicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|NavigationPropertySegment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|NavigationSegment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|SelectItem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|UriInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|expression
operator|.
name|FilterExpression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|expression
operator|.
name|OrderByExpression
import|;
end_import

begin_comment
comment|/**  * UriInfo with UriType information, determined in constructor.  */
end_comment

begin_class
DECL|class|UriInfoWithType
specifier|public
class|class
name|UriInfoWithType
implements|implements
name|UriInfo
block|{
DECL|field|uriInfo
specifier|private
specifier|final
name|UriInfo
name|uriInfo
decl_stmt|;
DECL|field|uriType
specifier|private
specifier|final
name|UriType
name|uriType
decl_stmt|;
DECL|method|UriInfoWithType (UriInfo uriInfo, String resourcePath)
specifier|public
name|UriInfoWithType
parameter_list|(
name|UriInfo
name|uriInfo
parameter_list|,
name|String
name|resourcePath
parameter_list|)
throws|throws
name|ODataApplicationException
throws|,
name|EdmException
block|{
name|this
operator|.
name|uriInfo
operator|=
name|uriInfo
expr_stmt|;
comment|// determine Uri Type
name|UriType
name|uriType
decl_stmt|;
specifier|final
name|List
argument_list|<
name|NavigationSegment
argument_list|>
name|segments
init|=
name|uriInfo
operator|.
name|getNavigationSegments
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|isLinks
init|=
name|uriInfo
operator|.
name|isLinks
argument_list|()
decl_stmt|;
if|if
condition|(
name|segments
operator|.
name|isEmpty
argument_list|()
operator|&&
name|uriInfo
operator|.
name|getTargetType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI0
expr_stmt|;
if|if
condition|(
name|resourcePath
operator|.
name|endsWith
argument_list|(
literal|"$metadata"
argument_list|)
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resourcePath
operator|.
name|endsWith
argument_list|(
literal|"$batch"
argument_list|)
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI9
expr_stmt|;
block|}
block|}
else|else
block|{
specifier|final
name|EdmEntitySet
name|targetEntitySet
init|=
name|uriInfo
operator|.
name|getTargetEntitySet
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetEntitySet
operator|!=
literal|null
condition|)
block|{
specifier|final
name|boolean
name|isCount
init|=
name|uriInfo
operator|.
name|isCount
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|KeyPredicate
argument_list|>
name|keyPredicates
init|=
name|uriInfo
operator|.
name|getKeyPredicates
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyPredicates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|isCount
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI1
expr_stmt|;
block|}
else|else
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI15
expr_stmt|;
block|}
block|}
else|else
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI2
expr_stmt|;
if|if
condition|(
name|isCount
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI16
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uriInfo
operator|.
name|isValue
argument_list|()
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI17
expr_stmt|;
block|}
specifier|final
name|EdmTypeKind
name|targetKind
init|=
name|uriInfo
operator|.
name|getTargetType
argument_list|()
operator|.
name|getKind
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|targetKind
condition|)
block|{
case|case
name|SIMPLE
case|:
if|if
condition|(
name|segments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI5
expr_stmt|;
block|}
else|else
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI4
expr_stmt|;
block|}
break|break;
case|case
name|COMPLEX
case|:
name|uriType
operator|=
name|UriType
operator|.
name|URI3
expr_stmt|;
break|break;
case|case
name|ENTITY
case|:
specifier|final
name|List
argument_list|<
name|EdmProperty
argument_list|>
name|propertyPath
init|=
name|uriInfo
operator|.
name|getPropertyPath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|segments
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|propertyPath
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|many
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|propertyPath
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|EdmProperty
name|lastProperty
init|=
name|propertyPath
operator|.
name|get
argument_list|(
name|propertyPath
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|many
operator|=
name|lastProperty
operator|.
name|getMultiplicity
argument_list|()
operator|==
name|EdmMultiplicity
operator|.
name|MANY
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|NavigationSegment
name|lastSegment
init|=
name|segments
operator|.
name|get
argument_list|(
name|segments
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|many
operator|=
name|lastSegment
operator|.
name|getKeyPredicates
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|lastSegment
operator|.
name|getNavigationProperty
argument_list|()
operator|.
name|getMultiplicity
argument_list|()
operator|==
name|EdmMultiplicity
operator|.
name|MANY
expr_stmt|;
block|}
if|if
condition|(
name|isCount
condition|)
block|{
if|if
condition|(
name|many
condition|)
block|{
name|uriType
operator|=
name|isLinks
condition|?
name|UriType
operator|.
name|URI50B
else|:
name|UriType
operator|.
name|URI15
expr_stmt|;
block|}
else|else
block|{
name|uriType
operator|=
name|UriType
operator|.
name|URI50A
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|many
condition|)
block|{
name|uriType
operator|=
name|isLinks
condition|?
name|UriType
operator|.
name|URI7B
else|:
name|UriType
operator|.
name|URI6B
expr_stmt|;
block|}
else|else
block|{
name|uriType
operator|=
name|isLinks
condition|?
name|UriType
operator|.
name|URI7A
else|:
name|UriType
operator|.
name|URI6A
expr_stmt|;
block|}
block|}
block|}
break|break;
default|default:
throw|throw
operator|new
name|ODataApplicationException
argument_list|(
literal|"Unexpected property type "
operator|+
name|targetKind
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
specifier|final
name|EdmFunctionImport
name|functionImport
init|=
name|uriInfo
operator|.
name|getFunctionImport
argument_list|()
decl_stmt|;
specifier|final
name|EdmType
name|targetType
init|=
name|uriInfo
operator|.
name|getTargetType
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|isCollection
init|=
name|functionImport
operator|.
name|getReturnType
argument_list|()
operator|.
name|getMultiplicity
argument_list|()
operator|==
name|EdmMultiplicity
operator|.
name|MANY
decl_stmt|;
switch|switch
condition|(
name|targetType
operator|.
name|getKind
argument_list|()
condition|)
block|{
case|case
name|SIMPLE
case|:
name|uriType
operator|=
name|isCollection
condition|?
name|UriType
operator|.
name|URI13
else|:
name|UriType
operator|.
name|URI14
expr_stmt|;
break|break;
case|case
name|COMPLEX
case|:
name|uriType
operator|=
name|isCollection
condition|?
name|UriType
operator|.
name|URI11
else|:
name|UriType
operator|.
name|URI12
expr_stmt|;
break|break;
case|case
name|ENTITY
case|:
name|uriType
operator|=
name|UriType
operator|.
name|URI10
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|ODataApplicationException
argument_list|(
literal|"Invalid function return type "
operator|+
name|targetType
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
throw|;
block|}
block|}
block|}
name|this
operator|.
name|uriType
operator|=
name|uriType
expr_stmt|;
block|}
DECL|method|getUriType ()
specifier|public
name|UriType
name|getUriType
parameter_list|()
block|{
return|return
name|uriType
return|;
block|}
annotation|@
name|Override
DECL|method|getEntityContainer ()
specifier|public
name|EdmEntityContainer
name|getEntityContainer
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getEntityContainer
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getStartEntitySet ()
specifier|public
name|EdmEntitySet
name|getStartEntitySet
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getStartEntitySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTargetEntitySet ()
specifier|public
name|EdmEntitySet
name|getTargetEntitySet
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getTargetEntitySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFunctionImport ()
specifier|public
name|EdmFunctionImport
name|getFunctionImport
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getFunctionImport
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTargetType ()
specifier|public
name|EdmType
name|getTargetType
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getTargetType
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getKeyPredicates ()
specifier|public
name|List
argument_list|<
name|KeyPredicate
argument_list|>
name|getKeyPredicates
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getKeyPredicates
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTargetKeyPredicates ()
specifier|public
name|List
argument_list|<
name|KeyPredicate
argument_list|>
name|getTargetKeyPredicates
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getTargetKeyPredicates
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getNavigationSegments ()
specifier|public
name|List
argument_list|<
name|NavigationSegment
argument_list|>
name|getNavigationSegments
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getNavigationSegments
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getPropertyPath ()
specifier|public
name|List
argument_list|<
name|EdmProperty
argument_list|>
name|getPropertyPath
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getPropertyPath
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isCount ()
specifier|public
name|boolean
name|isCount
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|isCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isValue ()
specifier|public
name|boolean
name|isValue
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|isValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isLinks ()
specifier|public
name|boolean
name|isLinks
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|isLinks
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFormat ()
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getFormat
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFilter ()
specifier|public
name|FilterExpression
name|getFilter
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getFilter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInlineCount ()
specifier|public
name|InlineCount
name|getInlineCount
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getInlineCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOrderBy ()
specifier|public
name|OrderByExpression
name|getOrderBy
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getOrderBy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSkipToken ()
specifier|public
name|String
name|getSkipToken
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getSkipToken
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSkip ()
specifier|public
name|Integer
name|getSkip
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getSkip
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTop ()
specifier|public
name|Integer
name|getTop
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getTop
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExpand ()
specifier|public
name|List
argument_list|<
name|ArrayList
argument_list|<
name|NavigationPropertySegment
argument_list|>
argument_list|>
name|getExpand
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getExpand
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSelect ()
specifier|public
name|List
argument_list|<
name|SelectItem
argument_list|>
name|getSelect
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getSelect
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFunctionImportParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|EdmLiteral
argument_list|>
name|getFunctionImportParameters
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getFunctionImportParameters
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCustomQueryOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCustomQueryOptions
parameter_list|()
block|{
return|return
name|uriInfo
operator|.
name|getCustomQueryOptions
argument_list|()
return|;
block|}
block|}
end_class

end_unit

