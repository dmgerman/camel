begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.bulk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchemaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|XMLGregorianCalendar
import|;
end_import

begin_comment
comment|/**  *<p>Java class for JobInfo complex type.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;complexType name="JobInfo">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="operation" type="{http://www.force.com/2009/06/asyncapi/dataload}OperationEnum" minOccurs="0"/>  *&lt;element name="object" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="createdById" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="createdDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>  *&lt;element name="systemModstamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>  *&lt;element name="state" type="{http://www.force.com/2009/06/asyncapi/dataload}JobStateEnum" minOccurs="0"/>  *&lt;element name="externalIdFieldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="concurrencyMode" type="{http://www.force.com/2009/06/asyncapi/dataload}ConcurrencyModeEnum" minOccurs="0"/>  *&lt;element name="contentType" type="{http://www.force.com/2009/06/asyncapi/dataload}ContentType" minOccurs="0"/>  *&lt;element name="numberBatchesQueued" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="numberBatchesInProgress" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="numberBatchesCompleted" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="numberBatchesFailed" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="numberBatchesTotal" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="numberRecordsProcessed" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="numberRetries" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="apiVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="assignmentRuleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="numberRecordsFailed" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="totalProcessingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>  *&lt;element name="apiActiveProcessingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>  *&lt;element name="apexProcessingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"JobInfo"
argument_list|,
name|propOrder
operator|=
block|{
literal|"id"
block|,
literal|"operation"
block|,
literal|"object"
block|,
literal|"createdById"
block|,
literal|"createdDate"
block|,
literal|"systemModstamp"
block|,
literal|"state"
block|,
literal|"externalIdFieldName"
block|,
literal|"concurrencyMode"
block|,
literal|"contentType"
block|,
literal|"numberBatchesQueued"
block|,
literal|"numberBatchesInProgress"
block|,
literal|"numberBatchesCompleted"
block|,
literal|"numberBatchesFailed"
block|,
literal|"numberBatchesTotal"
block|,
literal|"numberRecordsProcessed"
block|,
literal|"numberRetries"
block|,
literal|"apiVersion"
block|,
literal|"assignmentRuleId"
block|,
literal|"numberRecordsFailed"
block|,
literal|"totalProcessingTime"
block|,
literal|"apiActiveProcessingTime"
block|,
literal|"apexProcessingTime"
block|}
argument_list|)
DECL|class|JobInfo
specifier|public
class|class
name|JobInfo
block|{
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|field|operation
specifier|protected
name|OperationEnum
name|operation
decl_stmt|;
DECL|field|object
specifier|protected
name|String
name|object
decl_stmt|;
DECL|field|createdById
specifier|protected
name|String
name|createdById
decl_stmt|;
annotation|@
name|XmlSchemaType
argument_list|(
name|name
operator|=
literal|"dateTime"
argument_list|)
DECL|field|createdDate
specifier|protected
name|XMLGregorianCalendar
name|createdDate
decl_stmt|;
annotation|@
name|XmlSchemaType
argument_list|(
name|name
operator|=
literal|"dateTime"
argument_list|)
DECL|field|systemModstamp
specifier|protected
name|XMLGregorianCalendar
name|systemModstamp
decl_stmt|;
DECL|field|state
specifier|protected
name|JobStateEnum
name|state
decl_stmt|;
DECL|field|externalIdFieldName
specifier|protected
name|String
name|externalIdFieldName
decl_stmt|;
DECL|field|concurrencyMode
specifier|protected
name|ConcurrencyModeEnum
name|concurrencyMode
decl_stmt|;
DECL|field|contentType
specifier|protected
name|ContentType
name|contentType
decl_stmt|;
DECL|field|numberBatchesQueued
specifier|protected
name|Integer
name|numberBatchesQueued
decl_stmt|;
DECL|field|numberBatchesInProgress
specifier|protected
name|Integer
name|numberBatchesInProgress
decl_stmt|;
DECL|field|numberBatchesCompleted
specifier|protected
name|Integer
name|numberBatchesCompleted
decl_stmt|;
DECL|field|numberBatchesFailed
specifier|protected
name|Integer
name|numberBatchesFailed
decl_stmt|;
DECL|field|numberBatchesTotal
specifier|protected
name|Integer
name|numberBatchesTotal
decl_stmt|;
DECL|field|numberRecordsProcessed
specifier|protected
name|Integer
name|numberRecordsProcessed
decl_stmt|;
DECL|field|numberRetries
specifier|protected
name|Integer
name|numberRetries
decl_stmt|;
DECL|field|apiVersion
specifier|protected
name|String
name|apiVersion
decl_stmt|;
DECL|field|assignmentRuleId
specifier|protected
name|String
name|assignmentRuleId
decl_stmt|;
DECL|field|numberRecordsFailed
specifier|protected
name|Integer
name|numberRecordsFailed
decl_stmt|;
DECL|field|totalProcessingTime
specifier|protected
name|Long
name|totalProcessingTime
decl_stmt|;
DECL|field|apiActiveProcessingTime
specifier|protected
name|Long
name|apiActiveProcessingTime
decl_stmt|;
DECL|field|apexProcessingTime
specifier|protected
name|Long
name|apexProcessingTime
decl_stmt|;
comment|/**      * Gets the value of the id property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the value of the id property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the operation property.      *      * @return possible object is      *         {@link OperationEnum }      */
DECL|method|getOperation ()
specifier|public
name|OperationEnum
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Sets the value of the operation property.      *      * @param value allowed object is      *              {@link OperationEnum }      */
DECL|method|setOperation (OperationEnum value)
specifier|public
name|void
name|setOperation
parameter_list|(
name|OperationEnum
name|value
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the object property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getObject ()
specifier|public
name|String
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
comment|/**      * Sets the value of the object property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setObject (String value)
specifier|public
name|void
name|setObject
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the createdById property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getCreatedById ()
specifier|public
name|String
name|getCreatedById
parameter_list|()
block|{
return|return
name|createdById
return|;
block|}
comment|/**      * Sets the value of the createdById property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setCreatedById (String value)
specifier|public
name|void
name|setCreatedById
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|createdById
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the createdDate property.      *      * @return possible object is      *         {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|getCreatedDate ()
specifier|public
name|XMLGregorianCalendar
name|getCreatedDate
parameter_list|()
block|{
return|return
name|createdDate
return|;
block|}
comment|/**      * Sets the value of the createdDate property.      *      * @param value allowed object is      *              {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|setCreatedDate (XMLGregorianCalendar value)
specifier|public
name|void
name|setCreatedDate
parameter_list|(
name|XMLGregorianCalendar
name|value
parameter_list|)
block|{
name|this
operator|.
name|createdDate
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the systemModstamp property.      *      * @return possible object is      *         {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|getSystemModstamp ()
specifier|public
name|XMLGregorianCalendar
name|getSystemModstamp
parameter_list|()
block|{
return|return
name|systemModstamp
return|;
block|}
comment|/**      * Sets the value of the systemModstamp property.      *      * @param value allowed object is      *              {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|setSystemModstamp (XMLGregorianCalendar value)
specifier|public
name|void
name|setSystemModstamp
parameter_list|(
name|XMLGregorianCalendar
name|value
parameter_list|)
block|{
name|this
operator|.
name|systemModstamp
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the state property.      *      * @return possible object is      *         {@link JobStateEnum }      */
DECL|method|getState ()
specifier|public
name|JobStateEnum
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
comment|/**      * Sets the value of the state property.      *      * @param value allowed object is      *              {@link JobStateEnum }      */
DECL|method|setState (JobStateEnum value)
specifier|public
name|void
name|setState
parameter_list|(
name|JobStateEnum
name|value
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the externalIdFieldName property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getExternalIdFieldName ()
specifier|public
name|String
name|getExternalIdFieldName
parameter_list|()
block|{
return|return
name|externalIdFieldName
return|;
block|}
comment|/**      * Sets the value of the externalIdFieldName property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setExternalIdFieldName (String value)
specifier|public
name|void
name|setExternalIdFieldName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|externalIdFieldName
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the concurrencyMode property.      *      * @return possible object is      *         {@link ConcurrencyModeEnum }      */
DECL|method|getConcurrencyMode ()
specifier|public
name|ConcurrencyModeEnum
name|getConcurrencyMode
parameter_list|()
block|{
return|return
name|concurrencyMode
return|;
block|}
comment|/**      * Sets the value of the concurrencyMode property.      *      * @param value allowed object is      *              {@link ConcurrencyModeEnum }      */
DECL|method|setConcurrencyMode (ConcurrencyModeEnum value)
specifier|public
name|void
name|setConcurrencyMode
parameter_list|(
name|ConcurrencyModeEnum
name|value
parameter_list|)
block|{
name|this
operator|.
name|concurrencyMode
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the contentType property.      *      * @return possible object is      *         {@link ContentType }      */
DECL|method|getContentType ()
specifier|public
name|ContentType
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
comment|/**      * Sets the value of the contentType property.      *      * @param value allowed object is      *              {@link ContentType }      */
DECL|method|setContentType (ContentType value)
specifier|public
name|void
name|setContentType
parameter_list|(
name|ContentType
name|value
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberBatchesQueued property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberBatchesQueued ()
specifier|public
name|Integer
name|getNumberBatchesQueued
parameter_list|()
block|{
return|return
name|numberBatchesQueued
return|;
block|}
comment|/**      * Sets the value of the numberBatchesQueued property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberBatchesQueued (Integer value)
specifier|public
name|void
name|setNumberBatchesQueued
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberBatchesQueued
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberBatchesInProgress property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberBatchesInProgress ()
specifier|public
name|Integer
name|getNumberBatchesInProgress
parameter_list|()
block|{
return|return
name|numberBatchesInProgress
return|;
block|}
comment|/**      * Sets the value of the numberBatchesInProgress property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberBatchesInProgress (Integer value)
specifier|public
name|void
name|setNumberBatchesInProgress
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberBatchesInProgress
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberBatchesCompleted property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberBatchesCompleted ()
specifier|public
name|Integer
name|getNumberBatchesCompleted
parameter_list|()
block|{
return|return
name|numberBatchesCompleted
return|;
block|}
comment|/**      * Sets the value of the numberBatchesCompleted property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberBatchesCompleted (Integer value)
specifier|public
name|void
name|setNumberBatchesCompleted
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberBatchesCompleted
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberBatchesFailed property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberBatchesFailed ()
specifier|public
name|Integer
name|getNumberBatchesFailed
parameter_list|()
block|{
return|return
name|numberBatchesFailed
return|;
block|}
comment|/**      * Sets the value of the numberBatchesFailed property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberBatchesFailed (Integer value)
specifier|public
name|void
name|setNumberBatchesFailed
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberBatchesFailed
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberBatchesTotal property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberBatchesTotal ()
specifier|public
name|Integer
name|getNumberBatchesTotal
parameter_list|()
block|{
return|return
name|numberBatchesTotal
return|;
block|}
comment|/**      * Sets the value of the numberBatchesTotal property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberBatchesTotal (Integer value)
specifier|public
name|void
name|setNumberBatchesTotal
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberBatchesTotal
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberRecordsProcessed property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberRecordsProcessed ()
specifier|public
name|Integer
name|getNumberRecordsProcessed
parameter_list|()
block|{
return|return
name|numberRecordsProcessed
return|;
block|}
comment|/**      * Sets the value of the numberRecordsProcessed property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberRecordsProcessed (Integer value)
specifier|public
name|void
name|setNumberRecordsProcessed
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberRecordsProcessed
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberRetries property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberRetries ()
specifier|public
name|Integer
name|getNumberRetries
parameter_list|()
block|{
return|return
name|numberRetries
return|;
block|}
comment|/**      * Sets the value of the numberRetries property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberRetries (Integer value)
specifier|public
name|void
name|setNumberRetries
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberRetries
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the apiVersion property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
comment|/**      * Sets the value of the apiVersion property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setApiVersion (String value)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the assignmentRuleId property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getAssignmentRuleId ()
specifier|public
name|String
name|getAssignmentRuleId
parameter_list|()
block|{
return|return
name|assignmentRuleId
return|;
block|}
comment|/**      * Sets the value of the assignmentRuleId property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setAssignmentRuleId (String value)
specifier|public
name|void
name|setAssignmentRuleId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|assignmentRuleId
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberRecordsFailed property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberRecordsFailed ()
specifier|public
name|Integer
name|getNumberRecordsFailed
parameter_list|()
block|{
return|return
name|numberRecordsFailed
return|;
block|}
comment|/**      * Sets the value of the numberRecordsFailed property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberRecordsFailed (Integer value)
specifier|public
name|void
name|setNumberRecordsFailed
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberRecordsFailed
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the totalProcessingTime property.      *      * @return possible object is      *         {@link Long }      */
DECL|method|getTotalProcessingTime ()
specifier|public
name|Long
name|getTotalProcessingTime
parameter_list|()
block|{
return|return
name|totalProcessingTime
return|;
block|}
comment|/**      * Sets the value of the totalProcessingTime property.      *      * @param value allowed object is      *              {@link Long }      */
DECL|method|setTotalProcessingTime (Long value)
specifier|public
name|void
name|setTotalProcessingTime
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|totalProcessingTime
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the apiActiveProcessingTime property.      *      * @return possible object is      *         {@link Long }      */
DECL|method|getApiActiveProcessingTime ()
specifier|public
name|Long
name|getApiActiveProcessingTime
parameter_list|()
block|{
return|return
name|apiActiveProcessingTime
return|;
block|}
comment|/**      * Sets the value of the apiActiveProcessingTime property.      *      * @param value allowed object is      *              {@link Long }      */
DECL|method|setApiActiveProcessingTime (Long value)
specifier|public
name|void
name|setApiActiveProcessingTime
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|apiActiveProcessingTime
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the apexProcessingTime property.      *      * @return possible object is      *         {@link Long }      */
DECL|method|getApexProcessingTime ()
specifier|public
name|Long
name|getApexProcessingTime
parameter_list|()
block|{
return|return
name|apexProcessingTime
return|;
block|}
comment|/**      * Sets the value of the apexProcessingTime property.      *      * @param value allowed object is      *              {@link Long }      */
DECL|method|setApexProcessingTime (Long value)
specifier|public
name|void
name|setApexProcessingTime
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|apexProcessingTime
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

