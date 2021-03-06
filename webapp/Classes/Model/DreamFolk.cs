// DreamFolk.cs created with MonoDevelop
// User: damien at 4:14 PM 10/25/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using System;
using EmergeTk;
using EmergeTk.Model;
using EmergeTk.Widgets.Html;
using EmergeTk.Model.Security;
using DreamSpell;

namespace DreamSpell.Model
{
	public class DreamFolk : AbstractRecord
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(DreamFolk));
		
		string facebook_id,first_name,last_name; 
		DateTime birthday,last_update;
		int kin;		
		RecordList<DreamFolk> friends;
		string pictureBigUrl,pictureUrl,pictureSmallUrl,pictureSquareUrl;
		Tone tone;
		Glyph seal,guide,analog,occult,antipode;
		bool updated = false;
		
		public DreamFolk()
		{
		}
						
		public DreamFolk(DateTime bday)
		{
			this.Birthday = bday;
//			if (Main.Current.CurrentDreamFriend != null)
//			{
//				this.FacebookId = Main.Current.CurrentDreamFriend.FacebookId;
//				log.Warn("Set DateTime facebookid",this.FacebookId);
//				
//				this.Friends = Main.Current.CurrentDreamFriend.Friends;
//				log.Warn("Set DateTime friends",this.Friends.Count);
//			}
//			else
//				log.Warn("Didn't set DateTime to current dreamfriend",Main.Current.CurrentDreamFriend );
		}

		public DreamFolk(Facebook.Entity.User fb_user)
		{
			this.update(fb_user);
			
			log.Debug("\n\nCreated new dreamfolk person: " + this.Birthday);		
		}
		
		public void update(Facebook.Entity.User fb_user)
		{
			this.FacebookId = fb_user.UserId;
			this.FirstName = fb_user.FirstName;
			this.LastName = fb_user.LastName;
			this.Birthday = (DateTime)fb_user.Birthday;
			this.PictureUrl = Convert.ToString( fb_user.PictureUrl );
			this.PictureBigUrl = Convert.ToString( fb_user.PictureBigUrl );
			this.PictureSmallUrl = Convert.ToString( fb_user.PictureSmallUrl );
			this.PictureSquareUrl = Convert.ToString( fb_user.PictureSquareUrl );
			
			log.Debug("\n\nUpdated dreamfolk person: " + this.Birthday);			
		}

		public string GetName()
		{
			if ( Seal == null || Seal.Number == 0 ) return "<a target=\"_parent\" href=\"http://apps.facebook.com/dreamspell/index.aspx?invite=1\" style=\"background-color:#3B5998;border-color:#D9DFEA #0E1F5B #0E1F5B #D9DFEA;border-style:solid;border-width:1px;color:white;margin-right:5px;padding:3px 10px;line-height:24px\">Invite!</a>";;
			string seal = DreamSpellUtil.Seals[Seal.Number,0];
			//log.Debug("seal is: " + seal);			
			seal = seal.Replace(" "," " + DreamSpellUtil.Tones[Tone.Number,0] + " ");
			return seal;
		}
		
		public string Name
		{
			get { return GetName(); }
		}
		
		public string DreamName
		{
			get { return GetName(); }
		}
		
		public void Calc(DateTime bday)
		{
			//log.Debug("Calc dream folk bday: " + FirstName + " " + LastName,bday);
			if ( bday == null || bday.Year == 1900 || ( FacebookId != "Now" && bday.Year == DateTime.Now.Year ) ) 
			{
				this.Seal = new Glyph();
				this.Tone = new Tone();
				this.Guide = new Glyph();
				this.Analog = new Glyph();
				this.Occult = new Glyph();
				this.Antipode = new Glyph();
				this.Kin = 0;
				//log.Error("Invalid date set for birthday calc for " + FirstName + " " + LastName + " " + FacebookId,bday);
				return; 
			}
			DreamSpellUtil.Calc(bday);			
			
			//log.Debug("Current Tones: " + Main.Current.Tones.Count);
			//log.Debug("Util Tone: " + DreamSpellUtil.Tone);
				
			this.Tone = Main.Current.Tones[DreamSpellUtil.Tone];
			this.Seal = Main.Current.Glyphs[DreamSpellUtil.Seal];			
			this.Guide = Main.Current.Glyphs[DreamSpellUtil.Guide];
			this.Analog = Main.Current.Glyphs[DreamSpellUtil.Analog];
			this.Occult = Main.Current.Glyphs[DreamSpellUtil.Occult];
			this.Antipode = Main.Current.Glyphs[DreamSpellUtil.Antipode];
			this.Kin = DreamSpellUtil.Kin;
			//log.Debug("Calc dream folk bday finished: " + this.Seal + " " + this.Tone);
		}
		
		
		public bool IsFriend(string fb_id)
		{
			foreach (DreamFolk friend in Friends)
				if ( friend.facebook_id == fb_id ) return true;
			return false;		
		}
		
		public DreamFolk GetFriend(string fb_id)
		{
			if ( string.IsNullOrEmpty(fb_id) ) return null;
			foreach (DreamFolk friend in Friends)
				if ( friend.facebook_id == fb_id ) return friend;
			return null;		
		}
		
		IRecordList<DreamFolk> tones;
		public IRecordList<DreamFolk> Tones
		{
			get
			{
				if ( tones == null )
				{
					Friends.AddFilter(new FilterInfo("Tone",Tone));
					tones = Friends.Filter();
					Friends.Filters.Clear();
				}
				
				return tones;				
			}
		}
				
		IRecordList<DreamFolk> seals;
		public IRecordList<DreamFolk> Seals
		{
			get
			{
				if ( seals == null )
				{
					Friends.AddFilter(new FilterInfo("Seal",Seal));
					seals = Friends.Filter();
					Friends.Filters.Clear();
				}
				
				return seals;				
			}
		}
		
		IRecordList<DreamFolk> guides;
		public IRecordList<DreamFolk> Guides
		{
			get
			{
				if ( guides == null )
				{
					Friends.AddFilter(new FilterInfo("Seal",Guide));
					guides = Friends.Filter();
					Friends.Filters.Clear();
				}
				
				return guides;				
			}
		}
		
		IRecordList<DreamFolk> friendsSharingGuide;
		public IRecordList<DreamFolk> FriendsSharingGuide
		{
			get
			{
				if ( friendsSharingGuide == null )
				{
					friendsSharingGuide = Friends.Copy();
					log.Debug("got copy of friends",friendsSharingGuide.Count);
					log.Debug("Guide is " + Guide,Guide.Name);
					friendsSharingGuide.AddFilter(new FilterInfo("Guide",Guide));
					friendsSharingGuide = friendsSharingGuide.Filter();
					log.Debug("after filter friends",friendsSharingGuide.Count);
					
					
				}
				return friendsSharingGuide;				
			}
		}
		
		
		IRecordList<DreamFolk> allies;
		public IRecordList<DreamFolk> Allies
		{
			get
			{
				if ( allies == null )
				{
					Friends.AddFilter(new FilterInfo("Seal",Analog));
					allies = Friends.Filter();
					Friends.Filters.Clear();
				}
				return allies;				
			}
		}
		
		IRecordList<DreamFolk> occults;
		public IRecordList<DreamFolk> Occults
		{
			get
			{
				if ( occults == null )
				{
					Friends.AddFilter(new FilterInfo("Seal",Occult));
					occults = Friends.Filter();
					Friends.Filters.Clear();
				}
				return occults;				
			}
		}
		
		IRecordList<DreamFolk> focusList;
		public IRecordList<DreamFolk> FocusList
		{
			get
			{
				if ( focusList == null )
				{
					Friends.AddFilter(new FilterInfo("Seal",Antipode));
					focusList = Friends.Filter();
					Friends.Filters.Clear();
				}
				return focusList;				
			}
		}
		
		
		public Tone Tone {
			get {
				return tone;
			}
			set {
				tone = value;
			}
		}			
		
		public Glyph Occult {
			get {
				return occult;
			}
			set {
				occult = value;
			}
		}
		
		public static EmergeTkLog Log {
			get {
				return log;
			}
		}
		
		public Glyph Guide {
			get {
				return guide;
			}
			set {
				guide = value;
			}
		}
		
		public string FacebookId {
			get {
				return facebook_id;
			}
			set {
				facebook_id = value;
			}
		}
		
		public DateTime Birthday {
			get {
				return birthday;
			}
			set {
				birthday = value;			
				Calc(birthday);
			}
		}
		
		public Glyph Antipode {
			get {
				return antipode;
			}
			set {
				antipode = value;
			}
		}
		
		public Glyph Analog {
			get {
				return analog;
			}
			set {
				analog = value;
			}
		}

		public int Kin {
			get {
				return kin;
			}
			set {
				kin = value;
			}
		}

		public RecordList<DreamFolk> Friends {
			get {
				 if (this.friends == null)
                {
                    this.lazyLoadProperty<DreamFolk>("Friends");
                }
                return this.friends;
			}
			set {
				friends = value;
			}
		}

		public string PictureBigUrl {
			get {
				return pictureBigUrl;
			}
			set {
				pictureBigUrl = value;
			}
		}

		public DateTime LastUpdate {
			get {
				return last_update;
			}
			set {
				last_update = value;
			}
		}

		public string LastName {
			get {
				return last_name;
			}
			set {
				last_name = value;
			}
		}

		public string FirstName {
			get {
				//log.Debug("Returning firstName",first_name);
				return first_name;
			}
			set {
				first_name = value;
				//log.Debug("Set firstName",first_name);
			}
		}

		public string PictureUrl {
			get {
				return pictureUrl;
			}
			set {
				pictureUrl = value;
			}
		}

		public string PictureSmallUrl {
			get {
				return pictureSmallUrl;
			}
			set {
				pictureSmallUrl = value;
			}
		}

		public Glyph Seal {
			get {
				return seal;
			}
			set {
				seal = value;
			}
		}

		public bool Updated {
			get {
				return updated;
			}
			set {
				updated = value;
			}
		}

		public string PictureSquareUrl {
			get {
				return pictureSquareUrl;
			}
			set {
				pictureSquareUrl = value;
			}
		}
		/*
		public override void Save()
        {
			this.LastUpdate = DateTime.Now;
            base.Save();
			this.SaveRelations("Friends");           
        }
		
		public override void Save(bool SaveChildren)
        {
			this.LastUpdate = DateTime.Now;
            base.Save(SaveChildren);
			this.SaveRelations("Friends");    			
        }
        */	

	}
}
