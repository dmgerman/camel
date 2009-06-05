begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.jaxrs.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_interface
annotation|@
name|WebService
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/xml"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
DECL|interface|BookStore
specifier|public
interface|interface
name|BookStore
block|{
annotation|@
name|WebMethod
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/{id}"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"*/*"
argument_list|)
DECL|method|getBook (@athParamR) @ebParamname = R) Long id)
name|Book
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"id"
argument_list|)
name|Long
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
function_decl|;
annotation|@
name|WebMethod
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books"
argument_list|)
DECL|method|addBook (@ebParamname = R) Book book)
name|Book
name|addBook
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"book"
argument_list|)
name|Book
name|book
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

