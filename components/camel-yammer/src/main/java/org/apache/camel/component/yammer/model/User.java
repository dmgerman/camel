begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
package|;
end_package

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
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|User
specifier|public
class|class
name|User
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"previous_companies"
argument_list|)
DECL|field|previousCompanies
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|previousCompanies
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"kids_names"
argument_list|)
DECL|field|kidsNames
specifier|private
name|String
name|kidsNames
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"activated_at"
argument_list|)
DECL|field|activatedAt
specifier|private
name|String
name|activatedAt
decl_stmt|;
DECL|field|interests
specifier|private
name|String
name|interests
decl_stmt|;
DECL|field|admin
specifier|private
name|String
name|admin
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"full_name"
argument_list|)
DECL|field|fullName
specifier|private
name|String
name|fullName
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"last_name"
argument_list|)
DECL|field|lastName
specifier|private
name|String
name|lastName
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"mugshot_url_template"
argument_list|)
DECL|field|mugshotUrlTemplate
specifier|private
name|String
name|mugshotUrlTemplate
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"mugshot_url"
argument_list|)
DECL|field|mugshotUrl
specifier|private
name|String
name|mugshotUrl
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"birth_date"
argument_list|)
DECL|field|birthDate
specifier|private
name|String
name|birthDate
decl_stmt|;
DECL|field|timezone
specifier|private
name|String
name|timezone
decl_stmt|;
DECL|field|location
specifier|private
name|String
name|location
decl_stmt|;
DECL|field|state
specifier|private
name|String
name|state
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"web_url"
argument_list|)
DECL|field|webUrl
specifier|private
name|String
name|webUrl
decl_stmt|;
DECL|field|stats
specifier|private
name|Stats
name|stats
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"show_ask_for_photo"
argument_list|)
DECL|field|showAskForPhoto
specifier|private
name|Boolean
name|showAskForPhoto
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"external_urls"
argument_list|)
DECL|field|externalUrls
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|externalUrls
decl_stmt|;
DECL|field|schools
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schools
decl_stmt|;
DECL|field|summary
specifier|private
name|String
name|summary
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"job_title"
argument_list|)
DECL|field|jobTitle
specifier|private
name|String
name|jobTitle
decl_stmt|;
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
DECL|field|expertise
specifier|private
name|String
name|expertise
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"network_domains"
argument_list|)
DECL|field|networkDomains
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|networkDomains
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"network_name"
argument_list|)
DECL|field|networkName
specifier|private
name|String
name|networkName
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"hire_date"
argument_list|)
DECL|field|hireDate
specifier|private
name|String
name|hireDate
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|guid
specifier|private
name|String
name|guid
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"significant_other"
argument_list|)
DECL|field|significantOther
specifier|private
name|String
name|significantOther
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"verified_admin"
argument_list|)
DECL|field|verifiedAdmin
specifier|private
name|String
name|verifiedAdmin
decl_stmt|;
DECL|field|settings
specifier|private
name|Settings
name|settings
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"can_broadcast"
argument_list|)
DECL|field|canBroadcast
specifier|private
name|String
name|canBroadcast
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"first_name"
argument_list|)
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
DECL|field|department
specifier|private
name|String
name|department
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"network_id"
argument_list|)
DECL|field|networkId
specifier|private
name|Long
name|networkId
decl_stmt|;
DECL|field|contact
specifier|private
name|Contact
name|contact
decl_stmt|;
DECL|method|getPreviousCompanies ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPreviousCompanies
parameter_list|()
block|{
return|return
name|previousCompanies
return|;
block|}
DECL|method|setPreviousCompanies (List<String> previousCompanies)
specifier|public
name|void
name|setPreviousCompanies
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|previousCompanies
parameter_list|)
block|{
name|this
operator|.
name|previousCompanies
operator|=
name|previousCompanies
expr_stmt|;
block|}
DECL|method|getKidsNames ()
specifier|public
name|String
name|getKidsNames
parameter_list|()
block|{
return|return
name|kidsNames
return|;
block|}
DECL|method|setKidsNames (String kidsNames)
specifier|public
name|void
name|setKidsNames
parameter_list|(
name|String
name|kidsNames
parameter_list|)
block|{
name|this
operator|.
name|kidsNames
operator|=
name|kidsNames
expr_stmt|;
block|}
DECL|method|getActivatedAt ()
specifier|public
name|String
name|getActivatedAt
parameter_list|()
block|{
return|return
name|activatedAt
return|;
block|}
DECL|method|setActivatedAt (String activatedAt)
specifier|public
name|void
name|setActivatedAt
parameter_list|(
name|String
name|activatedAt
parameter_list|)
block|{
name|this
operator|.
name|activatedAt
operator|=
name|activatedAt
expr_stmt|;
block|}
DECL|method|getInterests ()
specifier|public
name|String
name|getInterests
parameter_list|()
block|{
return|return
name|interests
return|;
block|}
DECL|method|setInterests (String interests)
specifier|public
name|void
name|setInterests
parameter_list|(
name|String
name|interests
parameter_list|)
block|{
name|this
operator|.
name|interests
operator|=
name|interests
expr_stmt|;
block|}
DECL|method|getAdmin ()
specifier|public
name|String
name|getAdmin
parameter_list|()
block|{
return|return
name|admin
return|;
block|}
DECL|method|setAdmin (String admin)
specifier|public
name|void
name|setAdmin
parameter_list|(
name|String
name|admin
parameter_list|)
block|{
name|this
operator|.
name|admin
operator|=
name|admin
expr_stmt|;
block|}
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
DECL|method|setFullName (String fullName)
specifier|public
name|void
name|setFullName
parameter_list|(
name|String
name|fullName
parameter_list|)
block|{
name|this
operator|.
name|fullName
operator|=
name|fullName
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
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
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
DECL|method|setLastName (String lastName)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
DECL|method|getMugshotUrlTemplate ()
specifier|public
name|String
name|getMugshotUrlTemplate
parameter_list|()
block|{
return|return
name|mugshotUrlTemplate
return|;
block|}
DECL|method|setMugshotUrlTemplate (String mugshotUrlTemplate)
specifier|public
name|void
name|setMugshotUrlTemplate
parameter_list|(
name|String
name|mugshotUrlTemplate
parameter_list|)
block|{
name|this
operator|.
name|mugshotUrlTemplate
operator|=
name|mugshotUrlTemplate
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getMugshotUrl ()
specifier|public
name|String
name|getMugshotUrl
parameter_list|()
block|{
return|return
name|mugshotUrl
return|;
block|}
DECL|method|setMugshotUrl (String mugshotUrl)
specifier|public
name|void
name|setMugshotUrl
parameter_list|(
name|String
name|mugshotUrl
parameter_list|)
block|{
name|this
operator|.
name|mugshotUrl
operator|=
name|mugshotUrl
expr_stmt|;
block|}
DECL|method|getBirthDate ()
specifier|public
name|String
name|getBirthDate
parameter_list|()
block|{
return|return
name|birthDate
return|;
block|}
DECL|method|setBirthDate (String birthDate)
specifier|public
name|void
name|setBirthDate
parameter_list|(
name|String
name|birthDate
parameter_list|)
block|{
name|this
operator|.
name|birthDate
operator|=
name|birthDate
expr_stmt|;
block|}
DECL|method|getTimezone ()
specifier|public
name|String
name|getTimezone
parameter_list|()
block|{
return|return
name|timezone
return|;
block|}
DECL|method|setTimezone (String timezone)
specifier|public
name|void
name|setTimezone
parameter_list|(
name|String
name|timezone
parameter_list|)
block|{
name|this
operator|.
name|timezone
operator|=
name|timezone
expr_stmt|;
block|}
DECL|method|getLocation ()
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
DECL|method|setLocation (String location)
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|setState (String state)
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getWebUrl ()
specifier|public
name|String
name|getWebUrl
parameter_list|()
block|{
return|return
name|webUrl
return|;
block|}
DECL|method|setWebUrl (String webUrl)
specifier|public
name|void
name|setWebUrl
parameter_list|(
name|String
name|webUrl
parameter_list|)
block|{
name|this
operator|.
name|webUrl
operator|=
name|webUrl
expr_stmt|;
block|}
DECL|method|getStats ()
specifier|public
name|Stats
name|getStats
parameter_list|()
block|{
return|return
name|stats
return|;
block|}
DECL|method|setStats (Stats stats)
specifier|public
name|void
name|setStats
parameter_list|(
name|Stats
name|stats
parameter_list|)
block|{
name|this
operator|.
name|stats
operator|=
name|stats
expr_stmt|;
block|}
DECL|method|getShowAskForPhoto ()
specifier|public
name|Boolean
name|getShowAskForPhoto
parameter_list|()
block|{
return|return
name|showAskForPhoto
return|;
block|}
DECL|method|setShowAskForPhoto (Boolean showAskForPhoto)
specifier|public
name|void
name|setShowAskForPhoto
parameter_list|(
name|Boolean
name|showAskForPhoto
parameter_list|)
block|{
name|this
operator|.
name|showAskForPhoto
operator|=
name|showAskForPhoto
expr_stmt|;
block|}
DECL|method|getExternalUrls ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExternalUrls
parameter_list|()
block|{
return|return
name|externalUrls
return|;
block|}
DECL|method|setExternalUrls (List<String> externalUrls)
specifier|public
name|void
name|setExternalUrls
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|externalUrls
parameter_list|)
block|{
name|this
operator|.
name|externalUrls
operator|=
name|externalUrls
expr_stmt|;
block|}
DECL|method|getSchools ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSchools
parameter_list|()
block|{
return|return
name|schools
return|;
block|}
DECL|method|setSchools (List<String> schools)
specifier|public
name|void
name|setSchools
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schools
parameter_list|)
block|{
name|this
operator|.
name|schools
operator|=
name|schools
expr_stmt|;
block|}
DECL|method|getSummary ()
specifier|public
name|String
name|getSummary
parameter_list|()
block|{
return|return
name|summary
return|;
block|}
DECL|method|setSummary (String summary)
specifier|public
name|void
name|setSummary
parameter_list|(
name|String
name|summary
parameter_list|)
block|{
name|this
operator|.
name|summary
operator|=
name|summary
expr_stmt|;
block|}
DECL|method|getJobTitle ()
specifier|public
name|String
name|getJobTitle
parameter_list|()
block|{
return|return
name|jobTitle
return|;
block|}
DECL|method|setJobTitle (String jobTitle)
specifier|public
name|void
name|setJobTitle
parameter_list|(
name|String
name|jobTitle
parameter_list|)
block|{
name|this
operator|.
name|jobTitle
operator|=
name|jobTitle
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Long id)
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getExpertise ()
specifier|public
name|String
name|getExpertise
parameter_list|()
block|{
return|return
name|expertise
return|;
block|}
DECL|method|setExpertise (String expertise)
specifier|public
name|void
name|setExpertise
parameter_list|(
name|String
name|expertise
parameter_list|)
block|{
name|this
operator|.
name|expertise
operator|=
name|expertise
expr_stmt|;
block|}
DECL|method|getNetworkDomains ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getNetworkDomains
parameter_list|()
block|{
return|return
name|networkDomains
return|;
block|}
DECL|method|setNetworkDomains (List<String> networkDomains)
specifier|public
name|void
name|setNetworkDomains
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|networkDomains
parameter_list|)
block|{
name|this
operator|.
name|networkDomains
operator|=
name|networkDomains
expr_stmt|;
block|}
DECL|method|getNetworkName ()
specifier|public
name|String
name|getNetworkName
parameter_list|()
block|{
return|return
name|networkName
return|;
block|}
DECL|method|setNetworkName (String networkName)
specifier|public
name|void
name|setNetworkName
parameter_list|(
name|String
name|networkName
parameter_list|)
block|{
name|this
operator|.
name|networkName
operator|=
name|networkName
expr_stmt|;
block|}
DECL|method|getHireDate ()
specifier|public
name|String
name|getHireDate
parameter_list|()
block|{
return|return
name|hireDate
return|;
block|}
DECL|method|setHireDate (String hireDate)
specifier|public
name|void
name|setHireDate
parameter_list|(
name|String
name|hireDate
parameter_list|)
block|{
name|this
operator|.
name|hireDate
operator|=
name|hireDate
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getGuid ()
specifier|public
name|String
name|getGuid
parameter_list|()
block|{
return|return
name|guid
return|;
block|}
DECL|method|setGuid (String guid)
specifier|public
name|void
name|setGuid
parameter_list|(
name|String
name|guid
parameter_list|)
block|{
name|this
operator|.
name|guid
operator|=
name|guid
expr_stmt|;
block|}
DECL|method|getSignificantOther ()
specifier|public
name|String
name|getSignificantOther
parameter_list|()
block|{
return|return
name|significantOther
return|;
block|}
DECL|method|setSignificantOther (String significantOther)
specifier|public
name|void
name|setSignificantOther
parameter_list|(
name|String
name|significantOther
parameter_list|)
block|{
name|this
operator|.
name|significantOther
operator|=
name|significantOther
expr_stmt|;
block|}
DECL|method|getVerifiedAdmin ()
specifier|public
name|String
name|getVerifiedAdmin
parameter_list|()
block|{
return|return
name|verifiedAdmin
return|;
block|}
DECL|method|setVerifiedAdmin (String verifiedAdmin)
specifier|public
name|void
name|setVerifiedAdmin
parameter_list|(
name|String
name|verifiedAdmin
parameter_list|)
block|{
name|this
operator|.
name|verifiedAdmin
operator|=
name|verifiedAdmin
expr_stmt|;
block|}
DECL|method|getSettings ()
specifier|public
name|Settings
name|getSettings
parameter_list|()
block|{
return|return
name|settings
return|;
block|}
DECL|method|setSettings (Settings settings)
specifier|public
name|void
name|setSettings
parameter_list|(
name|Settings
name|settings
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
block|}
DECL|method|getCanBroadcast ()
specifier|public
name|String
name|getCanBroadcast
parameter_list|()
block|{
return|return
name|canBroadcast
return|;
block|}
DECL|method|setCanBroadcast (String canBroadcast)
specifier|public
name|void
name|setCanBroadcast
parameter_list|(
name|String
name|canBroadcast
parameter_list|)
block|{
name|this
operator|.
name|canBroadcast
operator|=
name|canBroadcast
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getDepartment ()
specifier|public
name|String
name|getDepartment
parameter_list|()
block|{
return|return
name|department
return|;
block|}
DECL|method|setDepartment (String department)
specifier|public
name|void
name|setDepartment
parameter_list|(
name|String
name|department
parameter_list|)
block|{
name|this
operator|.
name|department
operator|=
name|department
expr_stmt|;
block|}
DECL|method|getNetworkId ()
specifier|public
name|Long
name|getNetworkId
parameter_list|()
block|{
return|return
name|networkId
return|;
block|}
DECL|method|setNetworkId (Long networkId)
specifier|public
name|void
name|setNetworkId
parameter_list|(
name|Long
name|networkId
parameter_list|)
block|{
name|this
operator|.
name|networkId
operator|=
name|networkId
expr_stmt|;
block|}
DECL|method|getContact ()
specifier|public
name|Contact
name|getContact
parameter_list|()
block|{
return|return
name|contact
return|;
block|}
DECL|method|setContact (Contact contact)
specifier|public
name|void
name|setContact
parameter_list|(
name|Contact
name|contact
parameter_list|)
block|{
name|this
operator|.
name|contact
operator|=
name|contact
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"User [previousCompanies="
operator|+
name|previousCompanies
operator|+
literal|", kidsNames="
operator|+
name|kidsNames
operator|+
literal|", activatedAt="
operator|+
name|activatedAt
operator|+
literal|", interests="
operator|+
name|interests
operator|+
literal|", admin="
operator|+
name|admin
operator|+
literal|", fullName="
operator|+
name|fullName
operator|+
literal|", name="
operator|+
name|name
operator|+
literal|", lastName="
operator|+
name|lastName
operator|+
literal|", mugshotUrlTemplate="
operator|+
name|mugshotUrlTemplate
operator|+
literal|", type="
operator|+
name|type
operator|+
literal|", mugshotUrl="
operator|+
name|mugshotUrl
operator|+
literal|", birthDate="
operator|+
name|birthDate
operator|+
literal|", timezone="
operator|+
name|timezone
operator|+
literal|", location="
operator|+
name|location
operator|+
literal|", state="
operator|+
name|state
operator|+
literal|", webUrl="
operator|+
name|webUrl
operator|+
literal|", stats="
operator|+
name|stats
operator|+
literal|", showAskForPhoto="
operator|+
name|showAskForPhoto
operator|+
literal|", externalUrls="
operator|+
name|externalUrls
operator|+
literal|", schools="
operator|+
name|schools
operator|+
literal|", summary="
operator|+
name|summary
operator|+
literal|", jobTitle="
operator|+
name|jobTitle
operator|+
literal|", id="
operator|+
name|id
operator|+
literal|", expertise="
operator|+
name|expertise
operator|+
literal|", networkDomains="
operator|+
name|networkDomains
operator|+
literal|", networkName="
operator|+
name|networkName
operator|+
literal|", hireDate="
operator|+
name|hireDate
operator|+
literal|", url="
operator|+
name|url
operator|+
literal|", guid="
operator|+
name|guid
operator|+
literal|", significantOther="
operator|+
name|significantOther
operator|+
literal|", verifiedAdmin="
operator|+
name|verifiedAdmin
operator|+
literal|", settings="
operator|+
name|settings
operator|+
literal|", canBroadcast="
operator|+
name|canBroadcast
operator|+
literal|", firstName="
operator|+
name|firstName
operator|+
literal|", department="
operator|+
name|department
operator|+
literal|", networkId="
operator|+
name|networkId
operator|+
literal|", contact="
operator|+
name|contact
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

